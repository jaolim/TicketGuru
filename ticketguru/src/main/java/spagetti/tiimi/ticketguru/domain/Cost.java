package spagetti.tiimi.ticketguru.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long costid;
    @ManyToOne
    @JoinColumn(name = "eventid")
    private Event event;
    private String type;
    private Double price;

    public Cost() {

    }

    public Cost(String type, Double price, Event event) {
        this.type = type;
        this.price = price;
        this.event = event;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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
        return "Type: " + type + ", Price: " + price + ", " + event;
    }

}
