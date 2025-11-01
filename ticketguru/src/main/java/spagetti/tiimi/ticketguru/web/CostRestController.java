package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.Exception.BadRequestException;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;

@CrossOrigin(origins = "*")
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
    
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/costs")
    @ResponseStatus(HttpStatus.OK)
    public List<Cost> getAllCosts() {
        return (List<Cost>) crepository.findAll();
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/costs/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cost getCostById(@PathVariable Long id) {
         return crepository.findById(id).orElseThrow(() -> new NotFoundException("Cost does not exist"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/costs")
    @ResponseStatus(HttpStatus.CREATED)
    public Cost createCost(@RequestBody Cost cost) {
        if (cost.getPrice() == null) {
            throw new BadRequestException("Missing required field: price");
        }
        if (cost.getEvent() == null || cost.getEvent().getEventid() == null) {
            throw new BadRequestException("Incorrect or missing Event ID");
        }
        if (cost.getType() == null || cost.getType().getTypeid() == null) {
            throw new BadRequestException("Incorrect or missing TicketType ID");
        }


        Event event = erepository.findById(cost.getEvent().getEventid()).orElseThrow(()-> new BadRequestException("Event not found"));
        TicketType type = trepository.findById(cost.getType().getTypeid()).orElseThrow(()-> new BadRequestException("Ticket type not found"));

        cost.setEvent(event);
        cost.setType(type);

        return crepository.save(cost);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/costs/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Cost updateCost(@PathVariable Long id, @RequestBody Cost updatedCost) {
        Optional<Cost> optionalCost = crepository.findById(id);
        if (!optionalCost.isPresent()) {
            throw new NotFoundException("Cost with ID " + id + " not found");
        }

        Cost existingCost = optionalCost.get();

        if (updatedCost.getPrice() != null) {
            existingCost.setPrice(updatedCost.getPrice());
        }

        if (updatedCost.getEvent() != null && updatedCost.getEvent().getEventid() != null) {
            Event event = erepository.findById(updatedCost.getEvent().getEventid()).orElseThrow(() -> new BadRequestException("Event not found"));
            existingCost.setEvent(event);
        }

        if (updatedCost.getType() != null && updatedCost.getType().getTypeid() != null) {
            TicketType type = trepository.findById(updatedCost.getType().getTypeid()).orElseThrow(() -> new BadRequestException("Ticket type not found"));
            existingCost.setType(type);
        }

        return crepository.save(existingCost);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/costs/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCost(@PathVariable Long id) {
         if (!crepository.existsById(id)) {
            throw new NotFoundException("Cost with ID " + id + " not found");
        }
        crepository.deleteById(id);
    }

}
