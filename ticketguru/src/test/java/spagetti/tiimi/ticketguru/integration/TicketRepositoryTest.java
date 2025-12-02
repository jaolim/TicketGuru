package spagetti.tiimi.ticketguru.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
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
public class TicketRepositoryTest {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CostRepository costRepository;

    @Autowired
    private VenueRepository vrepository;

    private Event event;
    private TicketType ticketType;
    private Sale sale;
    private Cost cost;
    private Venue venue;

    @BeforeEach
    public void setup() {
        venue = new Venue("name", "address");
        vrepository.save(venue);
        event = new Event("Event X", venue, LocalDateTime.now(), 10);
        eventRepository.save(event);

        ticketType = new TicketType("VIP");
        ticketTypeRepository.save(ticketType);

        cost = new Cost(ticketType, 10.0, event);
        costRepository.save(cost);

        sale = new Sale();
        sale.setTime(LocalDateTime.now());
        sale.setPrice(10.0);
        saleRepository.save(sale);
    }

    @AfterEach
    public void cleanup() {
        ticketRepository.deleteAll();
        costRepository.deleteAll();
        saleRepository.deleteAll();
        ticketTypeRepository.deleteAll();
        eventRepository.deleteAll();
        vrepository.deleteAll();
    }

    @Test
    public void shouldCreateTicketWithCostOnly() {
        Ticket ticket = new Ticket(cost);
        ticketRepository.save(ticket);

        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketid());
        assertTrue(found.isPresent());
        assertEquals(cost.getCostid(), found.get().getCost().getCostid());
        assertFalse(found.get().getRedeemed());
    }

    @Test
    public void shouldCreateTicketWithCostAndSale() {
        Ticket ticket = new Ticket(cost, sale);
        ticketRepository.save(ticket);

        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketid());
        assertTrue(found.isPresent());
        assertEquals(cost.getCostid(), found.get().getCost().getCostid());
        assertEquals(sale.getSaleid(), found.get().getSale().getSaleid());
        assertFalse(found.get().getRedeemed());
    }

    @Test
    public void shouldUpdateRedeemed() {
        Ticket ticket = new Ticket(cost);
        ticketRepository.save(ticket);

        ticket.setRedeemed(true);
        ticketRepository.save(ticket);

        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketid());
        assertTrue(found.isPresent());
        assertTrue(found.get().getRedeemed());
    }

    @Test
    public void shouldDeleteTicket() {
        Ticket ticket = new Ticket(cost);
        ticketRepository.save(ticket);

        ticketRepository.deleteById(ticket.getTicketid());
        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketid());
        assertFalse(found.isPresent());
    }

    @Test
    public void shouldCreateTicketWithCostAndTicketType() {

        Cost cost = new Cost();
        cost.setPrice(35.0);
        cost.setEvent(event);
        cost.setTicketType(ticketType);
        costRepository.save(cost);

        Ticket ticket = new Ticket();
        ticket.setCost(cost);
        ticket.setRedeemed(false);
        ticketRepository.save(ticket);

        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketid());
        assertTrue(found.isPresent());

        assertEquals(35.0, found.get().getCost().getPrice());
        assertEquals(ticketType.getName(), found.get().getCost().getTicketType().getName());
    }

    @Test
    public void shouldLinkTicketToEventAndSale() {
        Ticket ticket = new Ticket(cost, sale);
        ticketRepository.save(ticket);

        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketid());
        assertTrue(found.isPresent());
        assertEquals(cost.getCostid(), found.get().getCost().getCostid());
        assertEquals(event.getEventid(), found.get().getCost().getEvent().getEventid());
        assertEquals(sale.getSaleid(), found.get().getSale().getSaleid());
    }

    @Test
    public void shouldCascadeDeleteSaleTickets() {
        Ticket ticket = new Ticket(cost, sale);
        ticketRepository.save(ticket);

        saleRepository.deleteById(sale.getSaleid());
        Optional<Ticket> found = ticketRepository.findById(ticket.getTicketid());
        assertFalse(found.isPresent(), "Ticket should be deleted when Sale is deleted (cascade)");
    }

    @Test
    public void shouldCascadeDeleteEventCostsTickets() {
        Ticket ticket = new Ticket(cost);
        ticketRepository.save(ticket);

        eventRepository.deleteById(event.getEventid());
        assertFalse(costRepository.findById(cost.getCostid()).isPresent());
        assertFalse(ticketRepository.findById(ticket.getTicketid()).isPresent());
    }

}
