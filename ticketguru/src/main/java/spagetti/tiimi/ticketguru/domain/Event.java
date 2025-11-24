package spagetti.tiimi.ticketguru.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import spagetti.tiimi.ticketguru.Views;

@Entity
public class Event {

    @JsonView(Views.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventid;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Event name is required")
    @Size(max = 200)
    private String name;

    @JsonView(Views.Public.class)
    @NotNull(message = "Start time is required")
    private LocalDateTime date;

    @JsonView(Views.Public.class)
    @NotNull(message = "Capacity is required")
    private Integer capacity;

    @JsonView(Views.Public.class)
    @JsonIgnoreProperties("event")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    // @JsonManagedReference
    private List<Cost> costs;

    @JsonView(Views.Public.class)
    @ManyToOne
    @JoinColumn(name = "venueid")
    private Venue venue;

    private Long totalTickets;

    public Event() {

    }

    public Event(String name, Venue venue, LocalDateTime date, Integer capacity) {
        this.name = name;
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
    }

    public void setEventid(Long eventid) {
        this.eventid = eventid;
    }

    public Long getEventid() {
        return eventid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public void setCosts(List<Cost> costs) {
        this.costs = costs;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public void setTotalTickets(Long totalTickets) {
        this.totalTickets = totalTickets;
    }

    public Long getTotalTickets() {
        return totalTickets;
    }

    @Override
    public String toString() {
        return "Event: " + name + ", Venue: " + venue + ", Date: " + date + ", Capacity: " + capacity;
    }

}
