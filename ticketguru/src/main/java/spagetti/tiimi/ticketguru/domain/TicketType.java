package spagetti.tiimi.ticketguru.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tickettypeid;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tickettype")
    private List<Cost> costs;

    private String name, note;

    public TicketType() {

    }

    public TicketType(String name) {
        this.name = name;
    }

    public void setTypeid(Long tickettypeid) {
        this.tickettypeid = tickettypeid;
    }

    public Long getTypeid() {
        return tickettypeid;
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
