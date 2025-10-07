package spagetti.tiimi.ticketguru.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ticketid;
    @ManyToOne
    @JoinColumn(name = "costid")
    private Cost cost;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "saleid")
    private Sale sale;

    private Boolean redeemed;
    private String name;

    public Ticket() {

    }

    public Ticket(String name) {
        this.name = name;
    }

    public Ticket(String name, Cost cost) {
        this.cost = cost;
        this.name = name;
        this.redeemed = false;
    }

        public Ticket(String name, Cost cost, Sale sale) {
        this.cost = cost;
        this.name = name;
        this.sale = sale;
        this.redeemed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTicketid(Long ticketid) {
        this.ticketid = ticketid;
    }

    public Long getTicketid() {
        return ticketid;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Sale getSale() {
        return sale;
    }

    public void setRedeemed(Boolean redeemed) {
        this.redeemed = redeemed;
    }

    public Boolean getRedeemed() {
        return redeemed;
    }

    @Override
    public String toString() {
        return "Ticket: [name: " + name + ", " + cost  + ", Redeemed: " + redeemed +"]";
    }

}
