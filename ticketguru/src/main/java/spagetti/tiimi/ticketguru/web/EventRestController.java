package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class EventRestController {

    private EventRepository erepository;

    public EventRestController(EventRepository erepository) {
        this.erepository = erepository;
    }

    @GetMapping(value = "/events")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> eventsRest() {
        return (List<Event>) erepository.findAll();
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Event> getEventRest(@PathVariable Long id) {
        return erepository.findById(id);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event newEvent(@RequestBody Event event) {
        return erepository.save(event);
    }

    @DeleteMapping("events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvent(@PathVariable Long id) {
        erepository.deleteById(id);
    }

    @PutMapping("events/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Event> editEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        return erepository.findById(id)
                .map(event -> {
                    event.setName(updatedEvent.getName());
                    event.setVenue(updatedEvent.getVenue());
                    event.setStart(updatedEvent.getStart());
                    return erepository.save(event);
                });
    }

}
