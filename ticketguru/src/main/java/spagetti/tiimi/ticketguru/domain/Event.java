package spagetti.tiimi.ticketguru.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventid;
    private String name;
    private String venue;
    private LocalDateTime start;

    @JsonIgnoreProperties("event")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<Cost> costs;

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

    public Long getEventid(){
        return eventid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setVenue(String venue){
        this.venue = venue;
    }

    public String getVenue(){
        return venue;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public void setCosts(List<Cost> costs) {
        this.costs = costs;
    }

    @Override
    public String toString(){
        return "Event: " + name + ", Venue: " + venue + ", Start: " + start;
    }

}
