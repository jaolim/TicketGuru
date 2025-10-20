package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;

@RestController
public class EventRestController {

    private EventRepository erepository;

    public EventRestController(EventRepository erepository) {
        this.erepository = erepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/events")
    public List<Event> getAllEvents() {
        return (List<Event>) erepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable Long id) {
        return erepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@Valid @RequestBody Event event) {
        return erepository.save(event);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("events/{id}")
    public void deleteEvent(@PathVariable Long id) {
        if (!erepository.findById(id).isPresent()) {
            throw new NotFoundException("Event does not exist");
        }
        erepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("events/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Event> editEvent(@PathVariable Long id, @Valid @RequestBody Event updatedEvent) {

        if (!erepository.findById(id).isPresent()) {
            throw new NotFoundException("Event does not exist");
        }

        return erepository.findById(id)
                .map(event -> {
                    event.setName(updatedEvent.getName());
                    event.setVenue(updatedEvent.getVenue());
                    event.setDate(updatedEvent.getDate());
                    return erepository.save(event);
                });
    }

}
