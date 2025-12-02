package spagetti.tiimi.ticketguru.integration;

import static org.junit.jupiter.api.Assertions.*;

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
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;

@SpringBootTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eRepository;

    @Autowired
    private VenueRepository vRepository;

    @Autowired
    private CostRepository cRepository;

    @Autowired
    private TicketRepository tRepository;

    private Event event;
    private Venue venue;
    private Cost cost;
    private Ticket ticket;

    @BeforeEach
    public void setup() {
        venue = new Venue("name", "address");
        vRepository.save(venue);
        event = new Event("Event", venue, LocalDateTime.now(), 10);
        eRepository.save(event);
    }

    @AfterEach
    public void cleanup() {
        eRepository.deleteAll();
        vRepository.deleteAll();
    }

    @Test
    public void shouldCreateNewEvent() {
        Event newEvent = new Event("Concert", venue, LocalDateTime.now().plusDays(1), 10);
        eRepository.save(newEvent);

        Optional<Event> found = eRepository.findById(newEvent.getEventid());
        assertTrue(found.isPresent());
        assertEquals("Concert", found.get().getName());
    }

    @Test
    public void shouldReturnEventById() {
        Optional<Event> found = eRepository.findById(event.getEventid());
        assertTrue(found.isPresent());
        assertEquals(event.getName(), found.get().getName());
    }

    @Test
    public void shouldUpdateEventVenue() {
        Venue newVenue = new Venue("Updated Venue", "address");
        vRepository.save(newVenue);
        event.setVenue(newVenue);
        eRepository.save(event);
        Optional<Event> found = eRepository.findById(event.getEventid());
        assertTrue(found.isPresent());
        assertEquals(newVenue.getName(), found.get().getVenue().getName());
    }

    @Test
    public void shouldDeleteEvent() {
        eRepository.deleteById(event.getEventid());
        Optional<Event> found = eRepository.findById(event.getEventid());
        assertFalse(found.isPresent());
    }

    @Test
    public void shouldNotFindNonExistingEvent() {
        Optional<Event> found = eRepository.findById(9999L);
        assertFalse(found.isPresent());
    }

    @Test
    public void shouldCreateEventWithVenue() {
        Venue v = new Venue("Test Venue", "Test Address");
        vRepository.save(v);

        Event ev = new Event("Test Event", v, LocalDateTime.now().plusDays(3), 50);
        eRepository.save(ev);

        Optional<Event> found = eRepository.findById(ev.getEventid());
        assertTrue(found.isPresent());
        assertEquals("Test Event", found.get().getName());
        assertEquals("Test Venue", found.get().getVenue().getName());
    }

    @Test
    public void shouldLinkCostToEvent() {
        Venue v = new Venue("Venue X", "Address X");
        vRepository.save(v);

        Event ev = new Event("Link Cost Event", v, LocalDateTime.now().plusDays(5), 100);
        eRepository.save(ev);

        Cost cost = new Cost();
        cost.setPrice(25.0);
        cost.setEvent(ev);
        cRepository.save(cost);

        Optional<Cost> found = cRepository.findById(cost.getCostid());
        assertTrue(found.isPresent());
        assertEquals(25.0, found.get().getPrice());
        assertEquals(ev.getEventid(), found.get().getEvent().getEventid());
    }

}
