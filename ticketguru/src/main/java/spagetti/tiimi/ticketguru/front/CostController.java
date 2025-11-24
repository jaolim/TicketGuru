package spagetti.tiimi.ticketguru.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.TicketType;

@Controller
public class CostController {

    private final CostRepository crepository;
    private final EventRepository erepository;
    private final TicketTypeRepository trepository;

    public CostController(CostRepository crepository, EventRepository erepository, TicketTypeRepository trepository) {
        this.crepository = crepository;
        this.erepository = erepository;
        this.trepository = trepository;
    }

    // Show list of costs
    @GetMapping("/costpage")
    public String costList(Model model) {
        model.addAttribute("costs", crepository.findAll());
        return "costs";
    }

    // Show add form
    @GetMapping("/cost/add")
    public String showAddForm(Model model) {
        model.addAttribute("cost", new Cost());
        model.addAttribute("events", erepository.findAll());
        model.addAttribute("types", trepository.findAll());
        return "cost-add";
    }

    // Handle POST from add form
    @PostMapping("/cost/add")
    public String saveCost(@ModelAttribute Cost cost,
                           @RequestParam Long eventId,
                           @RequestParam Long typeId) {

        Event event = erepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        TicketType type = trepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Ticket type not found"));

        cost.setEvent(event);
        cost.setType(type);

        crepository.save(cost);
        return "redirect:/costpage";
    }

    // Show edit form
    @GetMapping("/cost/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        Cost cost = crepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cost not found"));

        model.addAttribute("cost", cost);
        model.addAttribute("events", erepository.findAll());
        model.addAttribute("types", trepository.findAll());

        return "cost-edit";
    }

    // Handle edit save
    @PostMapping("/cost/edit/{id}")
    public String saveEditedCost(@PathVariable Long id,
                                 @ModelAttribute Cost updatedCost,
                                 @RequestParam Long eventId,
                                 @RequestParam Long typeId) {

        Cost cost = crepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cost not found"));

        cost.setPrice(updatedCost.getPrice());

        Event event = erepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        TicketType type = trepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Ticket type not found"));

        cost.setEvent(event);
        cost.setType(type);

        crepository.save(cost);
        return "redirect:/costpage";
    }

    @PostMapping("/cost/delete/{id}")
    public String deleteCost(@PathVariable Long id) {
        Cost cost = crepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cost not found"));

        crepository.delete(cost);
        return "redirect:/costpage";
    }
}