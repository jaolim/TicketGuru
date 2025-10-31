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
    public Optional<Cost> getCostById(@PathVariable Long id) {
        Optional<Cost> cost = crepository.findById(id);
        if (!cost.isPresent()) {
            throw new NotFoundException("Cost does not exist");
        }
        return crepository.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/costs")
    @ResponseStatus(HttpStatus.CREATED)
    public Cost createCost(@RequestBody Cost cost) {
        if (cost.getPrice() == null) {
            throw new BadRequestException("Missing required field: price");
        }
        if (cost.getEventId() == null || !erepository.findById(cost.getEventId()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Event ID");
        }
        if (cost.getTicketTypeId() == null || !trepository.findById(cost.getTicketTypeId()).isPresent()) {
            throw new BadRequestException("Incorrect or missing TicketType ID");
        }

        Event event = erepository.findById(cost.getEventId()).get();
        TicketType type = trepository.findById(cost.getTicketTypeId()).get();

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

        if (updatedCost.getEventId() != null) {
            Optional<Event> event = erepository.findById(updatedCost.getEventId());
            if (!event.isPresent()) {
                throw new BadRequestException("Event with ID " + updatedCost.getEventId() + " not found");
            }
            existingCost.setEvent(event.get());
        }

        if (updatedCost.getTicketTypeId() != null) {
            Optional<TicketType> type = trepository.findById(updatedCost.getTicketTypeId());
            if (!type.isPresent()) {
                throw new BadRequestException("TicketType with ID " + updatedCost.getTicketTypeId() + " not found");
            }
            existingCost.setType(type.get());
        }

        return crepository.save(existingCost);
    }

    

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/costs/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCost(@PathVariable Long id) {
        if (!crepository.findById(id).isPresent()) {
            throw new NotFoundException("Cost with ID " + id + " not found");
        }
        crepository.deleteById(id);
    }

}
