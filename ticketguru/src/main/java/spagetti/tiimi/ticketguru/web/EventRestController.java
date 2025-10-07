package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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

    @GetMapping(value = "/events")
    public List<Event> eventsRest() {
        return (List<Event>) erepository.findAll();
    }

    @GetMapping("/events/{id}")
    public Event getEvent(@PathVariable Long id) {
        return erepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event newEvent(@Valid @RequestBody Event event) {
        return erepository.save(event);
    }

    @DeleteMapping("events/{id}")
    public void deleteEvent(@PathVariable Long id) {
        if (!erepository.findById(id).isPresent()) {
            throw new NotFoundException("Event does not exist");
        }
        erepository.deleteById(id);
    }

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
                    event.setStart(updatedEvent.getStart());
                    return erepository.save(event);
                });
    }

}
