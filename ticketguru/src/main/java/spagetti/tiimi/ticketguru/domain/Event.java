package spagetti.tiimi.ticketguru.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import spagetti.tiimi.ticketguru.Views;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Event {

    @JsonView(Views.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventid;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Event name is required")
    @Size(max = 200)
    private String name;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Venue is required")
    @Size(max = 200)
    private String venue;

    @JsonView(Views.Public.class)
    @NotNull(message = "Start time is required")
    private LocalDateTime date;

    @JsonView(Views.Public.class)
    @JsonIgnoreProperties("event")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    //@JsonManagedReference
    private List<Cost> costs;

    public Event() {

    }

    public Event(String name, String venue, LocalDateTime date) {
        this.name = name;
        this.venue = venue;
        this.date = date;
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

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getVenue() {
        return venue;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public void setCosts(List<Cost> costs) {
        this.costs = costs;
    }

    @Override
    public String toString() {
        return "Event: " + name + ", Venue: " + venue + ", Date: " + date;
    }

}
