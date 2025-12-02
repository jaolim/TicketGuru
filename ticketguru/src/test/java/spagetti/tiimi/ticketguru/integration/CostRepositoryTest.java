package spagetti.tiimi.ticketguru.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;

@SpringBootTest
public class CostRepositoryTest {

    @Autowired
    private CostRepository crepository;

    @Autowired
    private EventRepository erepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketTypeRepository ttrepository;

    @Autowired
    private VenueRepository vrepository;

    private Event event;
    private Venue venue;
    private TicketType ticketType;
    private Ticket ticket;

    @BeforeEach
    public void setup() {
        venue = new Venue("name", "address");
        vrepository.save(venue);
        event = new Event("Event", venue, LocalDateTime.now(), 10);
        erepository.save(event);
        ticketType = new TicketType("TicketTypeName");
        ttrepository.save(ticketType);
    }

    @AfterEach
    public void cleanup() {
        crepository.deleteAll();
        erepository.deleteAll();
        ttrepository.deleteAll();
        vrepository.deleteAll();
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

    @Test
    public void shouldThrowExceptionForNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cost(ticketType, -5.0, event);
        });
    }

}