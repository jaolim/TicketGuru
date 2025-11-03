package spagetti.tiimi.ticketguru.web;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import spagetti.tiimi.ticketguru.Views;
import spagetti.tiimi.ticketguru.Exception.BadRequestException;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.Exception.TicketAlreadyRedeemedException;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "*")
@RestController
public class TicketRestController {

    private TicketRepository trepository;
    private CostRepository crepository;
    private SaleRepository srepository;

    public TicketRestController(TicketRepository trepository, CostRepository crepository, SaleRepository srepository) {
        this.trepository = trepository;
        this.crepository = crepository;
        this.srepository = srepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/tickets", params = "!code")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public List<Ticket> getAllTickets() {
        return (List<Ticket>) trepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickets")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public Ticket getTicketByCode(@RequestParam String code) {
        if (code.isEmpty()){
            throw new BadRequestException("Include value for code or ");
        } else if (!trepository.existsByTicketCode(code)) {
            throw new NotFoundException("Ticket does not exist");
        }
        return trepository.findByTicketCode(code);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickets/{id}")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public Optional<Ticket> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = trepository.findById(id);
        if (!ticket.isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }
        return trepository.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/tickets")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Ticket> createTicket(@RequestBody Ticket ticket) {
        if (ticket.getTicketid() != null) {
            throw new BadRequestException("Do not include ticketid");
        } else if (ticket.getCost() == null || !crepository.findById(ticket.getCost().getCostid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Cost ID");
        } else if (ticket.getSale() == null || !srepository.findById(ticket.getSale().getSaleid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Sale ID");
        }
        Long id = trepository.save(ticket).getTicketid();
        Optional<Ticket> saved = trepository.findById(id);

        return saved.map(mapped -> {
            mapped.setTicketCode(Base64.getEncoder().encodeToString((id.toString() + mapped.getCost().getCostid() + mapped.getSale().getSaleid().toString()).getBytes()));
            return trepository.save(mapped);
        });
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("tickets/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.OK)
    public void deleteTicket(@PathVariable Long id) {
        if (!trepository.findById(id).isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }
        trepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("tickets/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Ticket> editTicket(@PathVariable Long id, @RequestBody Ticket updatedTicket) {
        if (updatedTicket.getCost() == null || !crepository.findById(updatedTicket.getCost().getCostid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Cost ID");
        } else if (updatedTicket.getSale() == null
                || !srepository.findById(updatedTicket.getSale().getSaleid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing Sale ID");
        } else if (!trepository.findById(id).isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }

        return trepository.findById(id)
                .map(ticket -> {
                    ticket.setCost(updatedTicket.getCost());
                    ticket.setSale(updatedTicket.getSale());
                    ticket.setRedeemed(updatedTicket.getRedeemed());
                    ticket.setTicketCode(Base64.getEncoder().encodeToString((id.toString() + updatedTicket.getCost().getCostid() + updatedTicket.getSale().getSaleid().toString()).getBytes()));
                    return trepository.save(ticket);
                });
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PatchMapping("tickets/{id}")
    @JsonView(Views.Internal.class)
    public Ticket setTicketUsed(@PathVariable Long id) {
        Optional<Ticket> optionalTicket = trepository.findById(id);

        if (!optionalTicket.isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }

        Ticket existingTicket = optionalTicket.get();

        if (existingTicket.getRedeemed() == true) {
            throw new TicketAlreadyRedeemedException("Ticket already used");
        }

        existingTicket.setRedeemed(true);
        existingTicket.setRedeemedTime(LocalDateTime.now());

        return trepository.save(existingTicket);

    }

}