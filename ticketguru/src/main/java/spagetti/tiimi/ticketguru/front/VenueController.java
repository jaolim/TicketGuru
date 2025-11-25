package spagetti.tiimi.ticketguru.front;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import java.util.Map;
import java.util.List;

import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;

@Controller
public class VenueController {

    private final VenueRepository vRepository;

    public VenueController(VenueRepository vRepository) {
        this.vRepository = vRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/venuepage")
    public String getVenueList(Model model) {
        model.addAttribute("venues", vRepository.findAll());
        model.addAttribute("entity", "venue");
        return "venues";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/venue/edit/{id}")
    public String editVenue(@PathVariable Long id, Model model) {

        Venue venue = vRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venue not found"));

        model.addAttribute("item", venue);
        model.addAttribute("title", "Edit Venue");
        model.addAttribute("entity", "venue");

        return "venue-edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/venue/edit/{id}")
    public String saveVenue(@PathVariable Long id,
            @RequestParam Map<String, String> allParams) {

        Venue venue = vRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venue not found"));

        venue.setName(allParams.get("name"));
        venue.setAddress(allParams.get("address"));

        vRepository.save(venue);

        return "redirect:/venuepage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/venue/delete/{id}")
    public String deleteVenue(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Venue venue = vRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venue not found"));

        List<Event> events = venue.getEvents();

        if (events != null && !events.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete venue because there are events linked to it.");
            return "redirect:/venuepage";
        }

        vRepository.delete(venue);
        return "redirect:/venuepage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/venue/add")
    public String addVenueForm(Model model) {
        model.addAttribute("venue", new Venue());
        model.addAttribute("title", "Add Venue");
        return "venue-add";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/venue/add")
    public String saveNewVenue(@ModelAttribute Venue venue) {
        vRepository.save(venue);
        return "redirect:/venuepage";
    }

}
