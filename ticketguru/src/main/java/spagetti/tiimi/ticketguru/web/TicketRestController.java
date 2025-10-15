package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.Exception.BadRequestException;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class TicketRestController {

    private TicketRepository repository;
    private CostRepository crepository;
    private SaleRepository srepository;

    public TicketRestController(TicketRepository repository, CostRepository crepository, SaleRepository srepository) {
        this.repository = repository;
        this.crepository = crepository;
        this.srepository = srepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickets")
    @ResponseStatus(HttpStatus.OK)
    public List<Ticket> ticketTypesRest() {
        return (List<Ticket>) repository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickets/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Ticket> getTicketTypeRest(@PathVariable Long id) {
        Optional<Ticket> ticket = repository.findById(id);
        if (!ticket.isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }
        return repository.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket newTicketTypeRest(@RequestBody Ticket ticket) {
        if (ticket.getTicketid() != null) {
            throw new BadRequestException("Do not include ticketid");
        } else if (ticket.getCost() == null || !crepository.findById(ticket.getCost().getCostid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Cost ID");
         } else if (ticket.getSale() == null || !srepository.findById(ticket.getSale().getSaleid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Sale ID");
        }
        
        return repository.save(ticket);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("tickets/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTicketTypeRest(@PathVariable Long id) {
        if (!repository.findById(id).isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }
        repository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("tickets/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Ticket> editTicketTypeRest(@PathVariable Long id, @RequestBody Ticket updatedTicket) {
        if (updatedTicket.getCost() == null || !crepository.findById(updatedTicket.getCost().getCostid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Cost ID");
        } else if (updatedTicket.getSale() == null
                || !srepository.findById(updatedTicket.getSale().getSaleid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Sale ID");
        } else if (!repository.findById(id).isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }

        return repository.findById(id)
                .map(ticket -> {
                    ticket.setName(updatedTicket.getName());
                    ticket.setCost(updatedTicket.getCost());
                    ticket.setSale(updatedTicket.getSale());
                    ticket.setRedeemed(updatedTicket.getRedeemed());
                    return repository.save(ticket);
                });

    }

}