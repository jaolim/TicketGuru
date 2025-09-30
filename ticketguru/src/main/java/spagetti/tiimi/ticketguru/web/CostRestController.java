package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.domain.Event;

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class CostRestController {

    private CostRepository crepository;
    private EventRepository erepository;
    private TicketTypeRepository trepository;

    public CostRestController(CostRepository crepository, EventRepository erepository, TicketTypeRepository trepository) {
        this.crepository = crepository;
        this.erepository = erepository;
        this.trepository = trepository;
    }
    
    @GetMapping("/costs")
    public List<Cost> getAllCosts() {
        return (List<Cost>) crepository.findAll();
    }

    @GetMapping("/costs/{id}")
    public Cost getCostById(@PathVariable Long id) {
        return crepository.findById(id).orElseThrow(()-> new RuntimeException("Cost by id not found"));
    }

    @PostMapping("/costs")
    public Cost createCost(@RequestBody Cost cost) {
        return crepository.save(cost);
    }

    @PutMapping("/costs/{id}")
    public Cost updateCost(@PathVariable Long id, @RequestBody Cost costDetails) {
        Cost cost = crepository.findById(id).orElseThrow(()-> new RuntimeException("Cost by id not found")); 
        cost.setPrice(costDetails.getPrice());
        return crepository.save(cost); 
    }

    @DeleteMapping("/costs/{id}")
    public void deleteCost(@PathVariable Long id) {
        crepository.deleteById(id);
    }

}
