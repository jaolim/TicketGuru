package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;

@RestController
public class SaleRestController {

    private SaleRepository srepository;

    public SaleRestController (SaleRepository srepository) {
        this.srepository = srepository;
    }
    
    @GetMapping("/sales")
    public List<Sale> salesRest() {
        return (List<Sale>) srepository.findAll();
    }

    @GetMapping("/sales/{id}")
    public Optional<Sale> getSaleRest (@PathVariable Long id) {
        return srepository.findById(id);
    }

    @PostMapping("/sales")
    public Sale createSale(@RequestBody Sale sale) {
        return srepository.save(sale);
    }

    @DeleteMapping("/sales/{id}")
    public void deleteSale(@PathVariable Long id) {
        srepository.deleteById(id);
    }

    @PutMapping("/sales/{id}")
    public Optional <Sale> editSaleRest(@PathVariable Long id, @RequestBody Sale updatedSale) {
        return srepository.findById(id)
            .map(sale -> {
                sale.setPrice(updatedSale.getPrice());
                return srepository.save(sale);
            });
    }

}
