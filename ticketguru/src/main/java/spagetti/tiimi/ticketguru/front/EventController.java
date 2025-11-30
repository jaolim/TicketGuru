package spagetti.tiimi.ticketguru.front;

import java.time.LocalDateTime;
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

import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;

@Controller
public class EventController {
    private final EventRepository eRepository;
    private final VenueRepository vRepository;

    public EventController(EventRepository eRepository, VenueRepository vRepository) {
        this.eRepository = eRepository;
        this.vRepository = vRepository;

    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/eventpage")
    public String getEventList(Model model) {
        model.addAttribute("events", eRepository.findAll());
        return "events";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/event/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Event> eventOpt = eRepository.findById(id);
        if (!eventOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event by the id of " + id + " does not exist");
            return "redirect:/eventpage";
        }

        Event event = eventOpt.get();
        model.addAttribute("item", event);
        model.addAttribute("venues", vRepository.findAll());
        model.addAttribute("title", "Edit Event");
        model.addAttribute("entity", "event");

        return "event-edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/event/edit/{id}")
    public String saveEvent(@PathVariable Long id,
            @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {

        Optional<Event> eventOpt = eRepository.findById(id);
        if (!eventOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event by the id of " + id + " does not exist");
            return "redirect:/eventpage";
        }

        Event event = eventOpt.get();
        event.setName(allParams.get("name"));
        event.setCapacity(Integer.parseInt(allParams.get("capacity")));
        event.setDate(LocalDateTime.parse(allParams.get("date")));

        Long venueId = Long.parseLong(allParams.get("venueId"));

        Optional<Venue> venueOpt = vRepository.findById(venueId);
        if (!venueOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Venue by the id of " + venueId + " does not exist");
            return "redirect:/eventpage";
        }
        event.setVenue(venueOpt.get());

        eRepository.save(event);

        return "redirect:/eventpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Event> eventOpt = eRepository.findById(id);
        if (!eventOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event by the id of " + id + " does not exist");
            return "redirect:/eventpage";
        }

        eRepository.deleteById(id);
        return "redirect:/eventpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/event/add")
    public String addEventForm(Model model) {

        model.addAttribute("venues", vRepository.findAll());
        return "event-add";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/event/add")
    public String addEvent(
            @RequestParam String name,
            @RequestParam String date,
            @RequestParam Long venueId,
            @RequestParam Integer capacity, RedirectAttributes redirectAttributes) {

        Event event = new Event();
        event.setName(name);
        event.setCapacity(capacity);
        event.setDate(LocalDateTime.parse(date));

        Optional<Venue> venueOpt = vRepository.findById(venueId);
        if (!venueOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Venue by the id of " + venueId + " does not exist");
            return "redirect:/eventpage";
        }
        event.setVenue(venueOpt.get());

        eRepository.save(event);

        return "redirect:/eventpage";
    }

}
