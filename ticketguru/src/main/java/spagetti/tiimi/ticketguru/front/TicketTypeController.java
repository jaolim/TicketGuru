package spagetti.tiimi.ticketguru.front;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;

@Controller
public class TicketTypeController {

    private final TicketTypeRepository tRepository;

    public TicketTypeController(TicketTypeRepository tRepository) {
        this.tRepository = tRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickettypepage")
    public String getTicketTypeList(Model model) {
        model.addAttribute("tickettypes", tRepository.findAll());
        return "tickettypes";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickettype/edit/{id}")
    public String editTicketType(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<TicketType> ticketTypeOpt = tRepository.findById(id);

        if (!ticketTypeOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket type by the id of " + id + " does not exist");
            return "redirect:/tickettypepage";
        }
        
        TicketType ticketType = ticketTypeOpt.get();

        model.addAttribute("tickettype", ticketType);
        return "tickettype-edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/tickettype/edit/{id}")
    public String saveTicketType(@PathVariable Long id, @RequestParam Map<String, String> allParams,
            RedirectAttributes redirectAttributes) {
        Optional<TicketType> ticketTypeOpt = tRepository.findById(id);

        if (!ticketTypeOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket type by the id of " + id + " does not exist");
            return "redirect:/tickettypepage";
        }
        
        TicketType ticketType = ticketTypeOpt.get();
        ticketType.setName(allParams.get("name"));
        tRepository.save(ticketType);

        return "redirect:/tickettypepage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/tickettype/delete/{id}")
    public String deleteTicketType(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<TicketType> ticketTypeOpt = tRepository.findById(id);

        if (!ticketTypeOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket type by the id of " + id + " does not exist");
            return "redirect:/tickettypepage";
        }

        TicketType ticketType = ticketTypeOpt.get();
        boolean hasTickets = false;

        if (ticketType.getCosts() != null) {
            for (Cost cost : ticketType.getCosts()) {
                if (cost.getTickets() != null && !cost.getTickets().isEmpty()) {
                    hasTickets = true;
                    break;
                }
            }
        }
        if (hasTickets) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete ticket type that has tickets associated with it.");
            return "redirect:/tickettypepage";
        }

        tRepository.deleteById(id);
        return "redirect:/tickettypepage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickettype/add")
    public String addTicketTypeForm(Model model) {

        model.addAttribute("tickettypes", tRepository.findAll());
        return "tickettype-add";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/tickettype/add")
    public String addEvent(@RequestParam String name) {

        TicketType ticketType = new TicketType();
        ticketType.setName(name);
        tRepository.save(ticketType);

        return "redirect:/tickettypepage";
    }

}
