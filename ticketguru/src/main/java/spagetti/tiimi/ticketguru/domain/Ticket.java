package spagetti.tiimi.ticketguru.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ticketid;
    private String name;

    public Ticket () {

    }

    public Ticket (String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Ticket: [name: " + name + "]";
    }

}
