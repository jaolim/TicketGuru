package spagetti.tiimi.ticketguru.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import spagetti.tiimi.ticketguru.Views;

@Entity
public class TicketType {

    @JsonView(Views.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ticketTypeid;

    @JsonView(Views.Public.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketType")
    @JsonIgnore
    private List<Cost> costs;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Name required")
    private String name;

    @JsonView(Views.Public.class)
    private String note;

    public TicketType() {

    }

    public TicketType(String name) {
        this.name = name;
    }

    public TicketType(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public void setTypeid(Long ticketTypeid) {
        this.ticketTypeid = ticketTypeid;
    }

    public Long getTypeid() {
        return ticketTypeid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCosts(List<Cost> costs) {
        this.costs = costs;
    }

    public List<Cost> getCosts() {
        return costs;
    }

    @Override
    public String toString() {
        return name;
    }

}
