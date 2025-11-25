package spagetti.tiimi.ticketguru.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import spagetti.tiimi.ticketguru.Views;
import jakarta.persistence.Transient;

@Entity
public class Ticket {

    @JsonView(Views.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ticketid;

    @JsonView(Views.Public.class)
    @ManyToOne
    @JoinColumn(name = "costid")
    private Cost cost;

    @JsonView(Views.Public.class)
    @ManyToOne
    @JoinColumn(name = "saleid")
    @JsonIgnoreProperties("tickets")
    private Sale sale;

    @JsonView(Views.Public.class)
    private Boolean redeemed;
    @JsonView(Views.Public.class)
    private LocalDateTime redeemedTime;
    @JsonView(Views.Public.class)
    private String ticketCode;

    @Transient
    private Long selectedEventId;

    public Ticket() {

    }

    public Ticket(Cost cost) {
        this.cost = cost;
        this.redeemed = false;
    }

    public Ticket(Cost cost, Sale sale) {
        this.cost = cost;
        this.sale = sale;
        this.redeemed = false;
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

    public LocalDateTime getRedeemedTime() {
        return redeemedTime;
    }

    public void setRedeemedTime(LocalDateTime redeemedTime) {
        this.redeemedTime = redeemedTime;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public Long getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(Long selectedEventId) {
        this.selectedEventId = selectedEventId;
    }

    @Override
    public String toString() {
        return "Ticket: [" + cost + ", Redeemed: " + redeemed + "]";
    }

}