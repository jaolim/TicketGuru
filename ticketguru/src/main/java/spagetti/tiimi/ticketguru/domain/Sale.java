package spagetti.tiimi.ticketguru.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long saleid;

    @ManyToOne
    @JoinColumn(name = "userid")
    //@JsonIgnore
    private AppUser user;

    @NotNull(message = "Price is required")
    @Positive
    private double price;

    @NotNull(message = "Time of sale required")
    private LocalDateTime time;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sale")
    @JsonIgnore
    private List<Ticket> tickets;


    public Sale() {
    
    }

    public Sale(AppUser user, LocalDateTime time) {
        this.user = user;
        this.time = time;
    }

        public Sale(AppUser user, LocalDateTime time, double price) {
        this.user = user;
        this.time = time;
        this.price = price;
    }

    public Long getSaleid() {
        return saleid;
    }

    public void setSaleid(Long saleid) {
        this.saleid = saleid;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    @Override
    public String toString() {
        return "Sale [saleid=" + saleid + ", time=" + time + "]";
    }

}
