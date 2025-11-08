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

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;

@SpringBootTest
public class CostRepositoryTest {
    // TODO Toteutettavia testejä
    // hinnan laskeminen oikein

    @Autowired
    private CostRepository crepository;

    @Autowired
    private EventRepository erepository;

    @Autowired
    private TicketTypeRepository ttrepository;

    private Event event;
    private TicketType ticketType;

    @BeforeEach
    public void setup() {
        event = new Event("Event", "Venue", LocalDateTime.now());
        erepository.save(event);
        ticketType = new TicketType("TicketTypeName");
        ttrepository.save(ticketType);
    }

    @AfterEach
    public void cleanup() {
        crepository.deleteAll();
        erepository.deleteAll();
        ttrepository.deleteAll();
    }

    @Test
    public void shouldCreateNewCost() {
        Cost cost = new Cost(ticketType, 2.5, event);
        crepository.save(cost);
        Optional<Cost> found = crepository.findById(cost.getCostid());

        assertTrue(found.isPresent());
        assertEquals(2.5, found.get().getPrice());
    }

    @Test
    public void shouldReturnCostById() {
        Cost cost = new Cost(ticketType, 1.0, event);
        crepository.save(cost);
        Optional<Cost> found = crepository.findById(cost.getCostid());

        assertTrue(found.isPresent());
        assertEquals(cost.getCostid(), found.get().getCostid());
    }

    @Test
    public void shouldDeleteCost() {
        Cost cost = new Cost(ticketType, 3.0, event);
        crepository.save(cost);
        crepository.deleteById(cost.getCostid());

        Optional<Cost> found = crepository.findById(cost.getCostid());
        assertFalse(found.isPresent());
    }

    @Test
    public void shouldUpdatePrice() {
        Cost cost = new Cost(ticketType, 4.0, event);
        crepository.save(cost);

        cost.setPrice(9.99);
        crepository.save(cost);

        Optional<Cost> found = crepository.findById(cost.getCostid());
        assertTrue(found.isPresent());
        assertEquals(9.99, found.get().getPrice());
    }

}