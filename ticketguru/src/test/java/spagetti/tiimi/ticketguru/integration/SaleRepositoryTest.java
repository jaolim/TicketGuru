package spagetti.tiimi.ticketguru.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;

@SpringBootTest
public class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    private Sale sale;

    @BeforeEach
    public void setup() {
        sale = new Sale();
        sale.setTime(LocalDateTime.now());
        sale.setPrice(20.0);
        saleRepository.save(sale);
    }

    @AfterEach
    public void cleanup() {
        saleRepository.deleteAll();
    }

    @Test
    public void shouldCreateSale() {
        Sale newSale = new Sale();
        newSale.setTime(LocalDateTime.now().plusDays(1));
        newSale.setPrice(15.0);
        saleRepository.save(newSale);

        Optional<Sale> found = saleRepository.findById(newSale.getSaleid());
        assertTrue(found.isPresent());
        assertEquals(15.0, found.get().getPrice());
    }

    @Test
    public void shouldReturnSaleById() {
        Optional<Sale> found = saleRepository.findById(sale.getSaleid());
        assertTrue(found.isPresent());
        assertEquals(sale.getPrice(), found.get().getPrice());
    }

    @Test
    public void shouldUpdateSalePrice() {
        sale.setPrice(25.0);
        saleRepository.save(sale);

        Optional<Sale> found = saleRepository.findById(sale.getSaleid());
        assertTrue(found.isPresent());
        assertEquals(25.0, found.get().getPrice());
    }

    @Test
    public void shouldDeleteSale() {
        saleRepository.deleteById(sale.getSaleid());
        Optional<Sale> found = saleRepository.findById(sale.getSaleid());
        assertFalse(found.isPresent());
    }

    
}
