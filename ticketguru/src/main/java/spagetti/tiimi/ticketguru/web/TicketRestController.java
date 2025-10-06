package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.Exception.BadRequestException;
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

    @GetMapping("/tickets")
    public List<Ticket> ticketTypesRest() {
        return (List<Ticket>) repository.findAll();
    }

    @GetMapping("/tickets/{id}")
    public Optional<Ticket> getTicketTypeRest(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket newTicketTypeRest(@RequestBody Ticket ticket) {
        if (ticket.getCost() == null || !crepository.findById(ticket.getCost().getCostid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Cost" );
        } else if (ticket.getSale() == null || !srepository.findById(ticket.getSale().getSaleid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Sale");
        }
        return repository.save(ticket);
    }

    @DeleteMapping("tickets/{id}")
    public void deleteTicketTypeRest(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("tickets/{id}")
    public Optional<Ticket> editTicketTypeRest(@PathVariable Long id, @RequestBody Ticket updatedTicket) {
        if (updatedTicket.getCost() == null || !crepository.findById(updatedTicket.getCost().getCostid()).isPresent()) {
            return null;
        } else if (updatedTicket.getSale() == null || !srepository.findById(updatedTicket.getSale().getSaleid()).isPresent()) {
            return null;
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