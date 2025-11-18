package spagetti.tiimi.ticketguru.domain;

import java.util.List;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import spagetti.tiimi.ticketguru.Views;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @JsonView(Views.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long costid;

    @JsonView(Views.Public.class)
    @ManyToOne
    @JoinColumn(name = "eventid")
    @JsonIgnoreProperties("costs")
    //@JsonBackReference
    private Event event;

    @JsonView(Views.Public.class)
    @ManyToOne
    @JoinColumn(name = "ticketTypeid")
    private TicketType ticketType;

    @JsonView(Views.Public.class)
    //@NotNull(message = "Price is required")
    @Positive
    private Double price;

    @JsonView(Views.Public.class)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cost")
    private List<Ticket> tickets;

    public Cost() {

    }

    public Cost(TicketType ticketType, Double price, Event event) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
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
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
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
}