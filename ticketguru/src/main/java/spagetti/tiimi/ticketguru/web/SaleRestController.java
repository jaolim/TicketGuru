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

import com.fasterxml.jackson.annotation.JsonView;

import spagetti.tiimi.ticketguru.Views;
import spagetti.tiimi.ticketguru.Exception.BadRequestException;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

@CrossOrigin(originPatterns = "*")
@RestController
public class SaleRestController {

    private SaleRepository srepository;
    private AppUserRepository urepository;
    private EventRepository erepository;
    private TicketRepository trepository;

    public SaleRestController(SaleRepository srepository, AppUserRepository urepository, EventRepository erepository, TicketRepository trepository) {
        this.srepository = srepository;
        this.urepository = urepository;
        this.erepository = erepository;
        this.trepository = trepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sales")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public List<Sale> getAllSales() {
        return (List<Sale>) srepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sales/{id}")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public Optional<Sale> getSaleById(@PathVariable Long id) {
        Optional<Sale> sale = srepository.findById(id);
        if (!sale.isPresent()) {
            throw new NotFoundException("Sale does not exist");
        }
        return srepository.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/sales")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Sale createSale(@RequestBody Sale sale) {
        if (sale.getUser() == null || !urepository.findById(sale.getUser().getUserid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing User");
        }
        return srepository.save(sale);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/sales/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSale(@PathVariable Long id) {
        if (!srepository.findById(id).isPresent()) {
            throw new NotFoundException("Sale does not exist");
        }
        List<Event> events = erepository.findDistinctByCosts_Tickets_Sale_Saleid(id);
        srepository.deleteById(id);
        events.forEach(event -> {
            event.setTotalTickets(trepository.countByCost_Event_Eventid(event.getEventid()));
            erepository.save(event);
        });
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/sales/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Sale> editSale(@PathVariable Long id, @RequestBody Sale updatedSale) {
        Optional<Sale> oldSale = srepository.findById(id);
        if (!updatedSale.getDoorSale() && oldSale.get().getPrice() >= 0) {
            if (updatedSale.getPrice() <= 0){
                throw new BadRequestException("Price 0 is only acceptable for Door Sales");
            }
        } else if (updatedSale.getUser() == null || !urepository.findById(updatedSale.getUser().getUserid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing User");
        } else if (!oldSale.isPresent()) {
            throw new NotFoundException("Sale does not exist");
        }

        return srepository.findById(id)
                .map(sale -> {
                    sale.setPrice(updatedSale.getPrice());
                    sale.setTime(updatedSale.getTime());
                    return srepository.save(sale);
                });
    }

}
