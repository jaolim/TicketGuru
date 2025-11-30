package spagetti.tiimi.ticketguru.front;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;

@Controller
public class CostController {

    private final CostRepository crepository;
    private final EventRepository erepository;
    private final TicketTypeRepository trepository;
    private final TicketRepository repository;

    public CostController(CostRepository crepository, EventRepository erepository, TicketTypeRepository trepository,
            TicketRepository repository) {
        this.crepository = crepository;
        this.erepository = erepository;
        this.trepository = trepository;
        this.repository = repository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/costpage")
    public String costList(Model model) {
        model.addAttribute("costs", crepository.findAll());
        return "costs";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/cost/add")
    public String showAddForm(Model model) {
        model.addAttribute("cost", new Cost());
        model.addAttribute("events", erepository.findAll());
        model.addAttribute("types", trepository.findAll());
        return "cost-add";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/cost/add")
    public String saveCost(@ModelAttribute Cost cost,
            @RequestParam Long eventId,
            @RequestParam Long typeId, RedirectAttributes redirectAttributes) {

        Optional<Event> eventOpt = erepository.findById(eventId);
        if (!eventOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event not found with id " + eventId);
            return "redirect:/cost/add";
        }
        Optional<TicketType> typeOpt = trepository.findById(typeId);
        if (!typeOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket type not found with id " + typeId);
            return "redirect:/cost/add";
        }

        cost.setEvent(eventOpt.get());
        cost.setType(typeOpt.get());

        crepository.save(cost);
        return "redirect:/costpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/cost/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

        Optional<Cost> costOpt = crepository.findById(id);
        if (!costOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cost not found with id " + id);
            return "redirect:/costpage";
        }

        Cost cost = costOpt.get();
        model.addAttribute("cost", cost);
        model.addAttribute("events", erepository.findAll());
        model.addAttribute("types", trepository.findAll());

        return "cost-edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/cost/edit/{id}")
    public String saveEditedCost(@PathVariable Long id,
            @ModelAttribute Cost updatedCost,
            @RequestParam Long eventId,
            @RequestParam Long typeId,
            RedirectAttributes redirectAttributes) {

        Optional<Cost> costOpt = crepository.findById(id);
        if (!costOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cost not found with id " + id);
            return "redirect:/costpage";
        }
        Cost cost = costOpt.get();
        cost.setPrice(updatedCost.getPrice());

        Optional<Event> eventOpt = erepository.findById(eventId);
        if (!eventOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event not found with id " + eventId);
            return "redirect:/costpage";
        }
        Event event = eventOpt.get();

        Optional<TicketType> typeOpt = trepository.findById(typeId);
        if (!typeOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket type not found with id " + typeId);
            return "redirect:/costpage";
        }
        TicketType type = typeOpt.get();

        if (cost.getEvent().getEventid() != eventId) {
            if (repository.countByCost_Costid(id) > 0) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Costs with tickets cannot have their event changed");
                return "redirect:/costpage";
            }
        }
        cost.setEvent(event);
        cost.setType(type);

        crepository.save(cost);
        return "redirect:/costpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/cost/delete/{id}")
    public String deleteCost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Cost> costOpt = crepository.findById(id);
        if (!costOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cost not found with id " + id);
            return "redirect:/costpage";
        }
        Cost cost = costOpt.get();
        if (repository.countByCost_Costid(id) > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Costs with tickets cannot be deleted.");
            return "redirect:/costpage";
        }

        crepository.delete(cost);
        return "redirect:/costpage";
    }
}