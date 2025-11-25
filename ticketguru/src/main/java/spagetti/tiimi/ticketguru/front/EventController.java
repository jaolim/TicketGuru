package spagetti.tiimi.ticketguru.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import java.util.Map;

import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;

@Controller
public class EventController {
    private final EventRepository eRepository;
    private final VenueRepository vRepository;
    private final CostRepository cRepository;

    public EventController(EventRepository eRepository, VenueRepository vRepository, CostRepository cRepository) {
        this.eRepository = eRepository;
        this.vRepository = vRepository;
        this.cRepository = cRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/eventpage")
    public String getEventList(Model model) {
        model.addAttribute("events", eRepository.findAll());
        return "events";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/event/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model) {

        Event event = eRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        model.addAttribute("item", event);
        model.addAttribute("venues", vRepository.findAll());
        model.addAttribute("title", "Edit Event");
        model.addAttribute("entity", "event");

        return "event-edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/event/edit/{id}")
    public String saveEvent(@PathVariable Long id,
            @RequestParam Map<String, String> allParams) {

        Event event = eRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        event.setName(allParams.get("name"));
        event.setCapacity(Integer.parseInt(allParams.get("capacity")));
        event.setDate(LocalDateTime.parse(allParams.get("date")));

        Long venueId = Long.parseLong(allParams.get("venueId"));
        Venue venue = vRepository.findById(venueId)
                .orElseThrow(() -> new NotFoundException("Venue not found"));
        event.setVenue(venue);

        eRepository.save(event);

        return "redirect:/eventpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        Event event = eRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        eRepository.delete(event);
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
            @RequestParam Integer capacity) {

        Event event = new Event();
        event.setName(name);
        event.setCapacity(capacity);
        event.setDate(LocalDateTime.parse(date));

        Venue venue = vRepository.findById(venueId)
                .orElseThrow(() -> new NotFoundException("Venue not found"));
        event.setVenue(venue);

        eRepository.save(event);

        return "redirect:/eventpage";
    }

}
