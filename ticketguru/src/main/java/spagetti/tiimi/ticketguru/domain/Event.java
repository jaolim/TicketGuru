package spagetti.tiimi.ticketguru.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventid;
    private String name;
    private String venue;
    private LocalDateTime start;

    public Event () {

    }

    public Event (String name, String venue, LocalDateTime start) {
        this.name = name;
        this.venue = venue;
        this.start = start;
    }

    public void setEventid(Long eventid) {
        this.eventid = eventid;
    }

    @Override
    public String toString(){
        return "Event: " + name + " Venue: " + venue + " Start: " + start;
    }

}
