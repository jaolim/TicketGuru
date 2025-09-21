package spagetti.tiimi.ticketguru.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class EventRestController {

    private EventRepository erepository;

    public EventRestController (EventRepository erepository) {
        this.erepository = erepository;
    }

    @GetMapping(value = "/events")
    public List<Event> eventsRest() {
        return (List<Event>) erepository.findAll();
    }

    @PostMapping("/events")
    public Event newEvent(@RequestBody Event event) {
        return erepository.save(event);
    }
    
    @DeleteMapping("events/{id}")
    public void deleteEvent(@PathVariable Long id) {
        erepository.deleteById(id);
    }

}
