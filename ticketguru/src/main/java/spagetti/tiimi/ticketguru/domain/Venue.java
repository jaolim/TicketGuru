package spagetti.tiimi.ticketguru.domain;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import spagetti.tiimi.ticketguru.Views;

@Entity
public class Venue {

    @JsonView(Views.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long venueid;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Venue must have a name")
    private String name;

    @JsonView(Views.Public.class)
    @NotNull(message = "Capacity is required")
    private Integer capacity;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Address is required")
    private String address;
   
    public Venue() {
    }

    public Venue(@NotBlank(message = "Venue must have a name") String name,
            @NotBlank(message = "Capacity is required") Integer capacity,
            @NotBlank(message = "Address is required") String address) {
        this.name = name;
        this.capacity = capacity;
        this.address = address;
    }

    public Long getVenueid() {
        return venueid;
    }

    public void setVenueid(Long venueid) {
        this.venueid = venueid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name + ", capacity: " + capacity + ", address: " + address + "]";
    }

    
}
