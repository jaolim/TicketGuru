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
import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(originPatterns = "*")
@RestController
public class TicketRestController {

    private TicketRepository trepository;
    private CostRepository crepository;
    private SaleRepository srepository;
    private EventRepository erepository;

    public TicketRestController(TicketRepository trepository, CostRepository crepository, SaleRepository srepository,
            EventRepository erepository) {
        this.trepository = trepository;
        this.crepository = crepository;
        this.srepository = srepository;
        this.erepository = erepository;
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
        if (code.isEmpty()) {
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
        Cost cost = crepository.findById(saved.get().getCost().getCostid()).get();
        saved.get().setPrice(cost.getPrice());

        if (cost.getEvent().getCapacity() <= cost.getEvent().getTotalTickets()) {
            throw new BadRequestException("Capacity for event exceeded");
        }
        ;

        Optional<Sale> sale = srepository.findById(ticket.getSale().getSaleid());
        if (sale.isPresent()) {
            sale.get().setPrice(sale.get().getPrice() + saved.get().getPrice());
            srepository.save(sale.get());
        }

        Optional<Event> event = erepository.findById(cost.getEvent().getEventid());

        if (event.isPresent()) {
            event.get().setTotalTickets(event.get().getTotalTickets() + 1);
        }

        return saved.map(mapped -> {
            mapped.setTicketCode(
                    Base64.getEncoder().encodeToString((id.toString() + mapped.getCost().getCostid()).getBytes()));
            return trepository.save(mapped);
        });
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("tickets/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.OK)
    public void deleteTicket(@PathVariable Long id) {
        Optional<Ticket> ticket = trepository.findById(id);
        if (!ticket.isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }
        Sale sale = ticket.get().getSale();
        if (sale != null) {
            sale.setPrice(sale.getPrice() - ticket.get().getPrice());
            srepository.save(sale);
        }

        Event event = ticket.get().getCost().getEvent();
        event.setTotalTickets(event.getTotalTickets() - 1);
        erepository.save(event);

        trepository.deleteById(id);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("tickets/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Ticket> editTicket(@PathVariable Long id, @RequestBody Ticket updatedTicket) {
        Optional<Ticket> oldTicket = trepository.findById(id);
        Optional<Cost> cost = crepository.findById(updatedTicket.getCost().getCostid());
        Optional<Sale> newSale = srepository.findById(updatedTicket.getSale().getSaleid());
        if (updatedTicket.getCost() == null || !cost.isPresent()) {
            throw new BadRequestException("Incorrect or missing Cost ID");
        } else if (updatedTicket.getSale() == null
                || !newSale.isPresent()) {
            throw new BadRequestException("Incorrect or missing Sale ID");
        } else if (!oldTicket.isPresent()) {
            throw new NotFoundException("Ticket does not exist");
        }

        Event oldEvent = oldTicket.get().getCost().getEvent();
        Event newEvent = cost.get().getEvent();

        if (oldEvent.getEventid() != newEvent.getEventid()) {
            if (newEvent.getCapacity() <= newEvent.getTotalTickets()) {
                throw new BadRequestException("Capacity for event exceeded");
            }
            oldEvent.setTotalTickets(oldEvent.getTotalTickets() - 1);
            newEvent.setTotalTickets(newEvent.getTotalTickets() + 1);
            erepository.save(oldEvent);
            erepository.save(newEvent);
        }

        Sale oldSale = oldTicket.get().getSale();
        if (oldSale != null) {
            double result = oldSale.getPrice() - oldTicket.get().getPrice();
            if (result < 0) {
                oldSale.setPrice(0);
            } else {
                oldSale.setPrice(result);
            }
        }
        if (newSale.isPresent()) {
            newSale.get().setPrice(newSale.get().getPrice() + cost.get().getPrice());
        }

        return trepository.findById(id)
                .map(ticket -> {
                    ticket.setCost(updatedTicket.getCost());
                    ticket.setSale(updatedTicket.getSale());
                    ticket.setRedeemed(updatedTicket.getRedeemed());
                    ticket.setTicketCode(Base64.getEncoder()
                            .encodeToString((id.toString() + updatedTicket.getCost().getCostid()).getBytes()));
                    ticket.setPrice(cost.get().getPrice());
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