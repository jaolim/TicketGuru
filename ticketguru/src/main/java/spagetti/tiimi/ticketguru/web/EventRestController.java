package spagetti.tiimi.ticketguru.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;

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


}
