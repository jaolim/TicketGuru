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
import spagetti.tiimi.ticketguru.domain.AppUserRepository;
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;

@CrossOrigin(origins = "*")
@RestController
public class SaleRestController {

    private SaleRepository srepository;
    private AppUserRepository urepository;

    public SaleRestController (SaleRepository srepository, AppUserRepository urepository) {
        this.srepository = srepository;
        this.urepository = urepository;
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sales")
    @ResponseStatus(HttpStatus.OK)
    public List<Sale> getAllSales() {
        return (List<Sale>) srepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sales/{id}")
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
    @ResponseStatus(HttpStatus.CREATED)
    public Sale createSale(@RequestBody Sale sale) {
        if (sale.getUser() == null || !urepository.findById(sale.getUser().getUserid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing User");
        }
        return srepository.save(sale);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/sales/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSale(@PathVariable Long id) {
        if (!srepository.findById(id).isPresent()) {
            throw new NotFoundException("Sale does not exist");
        }
        srepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/sales/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional <Sale> editSale(@PathVariable Long id, @RequestBody Sale updatedSale) {
        if (updatedSale.getUser() == null || !urepository.findById(updatedSale.getUser().getUserid()).isPresent()) {
            throw new BadRequestException("Incorrect or missing User");
        } else if (!srepository.findById(id).isPresent()) {
            throw new NotFoundException("Sale does not exist");
        }

        return srepository.findById(id)
            .map(sale -> {
                sale.setPrice(updatedSale.getPrice());
                return srepository.save(sale);
            });
    }

}
