package spagetti.tiimi.ticketguru.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import spagetti.tiimi.ticketguru.Views;

@Entity
public class Venue {

    @JsonView(Views.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long venueid;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Venue must have a name")
    private String name;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Address is required")
    private String address;

    @JsonView(Views.Public.class)
    @JsonIgnore
    @OneToMany(mappedBy = "venue")
    private List<Event> events;
   
    public Venue() {
    }

    public Venue(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getVenueid() {
        return venueid;
    }

    public void setVenueid(Long venueid) {
        this.venueid = venueid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return name + ", address: " + address + "]";
    }

    
}
