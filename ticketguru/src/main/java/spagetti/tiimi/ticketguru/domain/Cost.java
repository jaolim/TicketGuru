package spagetti.tiimi.ticketguru.domain;

import java.util.List;

import jakarta.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long costid;
    @ManyToOne
    @JoinColumn(name = "eventid")
    @JsonBackReference
    private Event event;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cost")
    private List<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "ticketTypeid")
    @JsonIgnore
    private TicketType ticketType;

    private Double price;

    public Cost() {

    }

    public Cost(TicketType ticketType, Double price, Event event) {
        this.ticketType = ticketType;
        this.price = price;
        this.event = event;
    }

    public void setType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public TicketType getType() {
        return ticketType;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public Long getCostid() {
        return costid;
    }

    public void setCostid(Long costid) {
        this.costid = costid;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Type: " + ticketType + ", Price: " + price + ", " + event;
    }

    @Transient
    @JsonProperty("eventId")
    private Long eventId;

    @Transient
    @JsonProperty("ticketTypeId")
    private Long ticketTypeId;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

}