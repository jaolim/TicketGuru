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

    @GetMapping("/eventpage")
    public String getEventList(Model model) {
        model.addAttribute("events", eRepository.findAll());
        return "events";
    }

    @GetMapping("/event/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editEvent(@PathVariable Long id, Model model) {
        Event event = eRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        model.addAttribute("event", event);
        model.addAttribute("venues", vRepository.findAll());
        model.addAttribute("costs", cRepository.findAll());
        return "event-edit";
    }

    @PostMapping("/event/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveEditedEvent(@PathVariable Long id,
            @RequestParam String name,
            @RequestParam("date") String dateStr,
            @RequestParam("venueId") Long venueId) {

        Event event = eRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        Venue venue = vRepository.findById(venueId)
                .orElseThrow(() -> new NotFoundException("Venue not found"));

        event.setName(name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
        event.setDate(date);

        event.setVenue(venue);

        eRepository.save(event);

        return "redirect:/eventpage";
    }

}
