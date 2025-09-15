package spagetti.tiimi.ticketguru.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long saleid;
    @ManyToOne
    @JoinColumn(name = "ticketid")
    private Ticket ticket;
    // private User user;
    private int price; // myytyjen lippujen kokonaishinta
    private LocalDateTime time;

    public Sale() {
    
    }

    public Sale(Ticket ticket, LocalDateTime time) {
        this.ticket = ticket;
        this.time = time;
    }

    public Long getSaleid() {
        return saleid;
    }

    public void setSaleid(Long saleid) {
        this.saleid = saleid;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "Sale [saleid=" + saleid + ", time=" + time + "]";
    }

}
