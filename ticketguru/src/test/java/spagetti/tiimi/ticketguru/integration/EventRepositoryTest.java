package spagetti.tiimi.ticketguru.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;

@SpringBootTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository ereporitory;

    @Autowired
    private VenueRepository vrepository;

    private Event event;
    private Venue venue;

    @BeforeEach
    public void setup() {
        venue = new Venue("name", "address");
        vrepository.save(venue);
        event = new Event("Event", venue, LocalDateTime.now(), 10);
        ereporitory.save(event);
    }

    @AfterEach
    public void cleanup() {
        ereporitory.deleteAll();
        vrepository.deleteAll();
    }

    @Test
    public void shouldCreateNewEvent() {
        Event newEvent = new Event("Concert", venue, LocalDateTime.now().plusDays(1), 10);
        ereporitory.save(newEvent);

        Optional<Event> found = ereporitory.findById(newEvent.getEventid());
        assertTrue(found.isPresent());
        assertEquals("Concert", found.get().getName());
    }

    @Test
    public void shouldReturnEventById() {
        Optional<Event> found = ereporitory.findById(event.getEventid());
        assertTrue(found.isPresent());
        assertEquals(event.getName(), found.get().getName());
    }

    @Test
    public void shouldUpdateEventVenue() {
        Venue newVenue = new Venue("Updated Venue", "address");
        vrepository.save(newVenue);
        event.setVenue(newVenue);
        ereporitory.save(event);
        Optional<Event> found = ereporitory.findById(event.getEventid());
        assertTrue(found.isPresent());
        assertEquals(newVenue.getName(), found.get().getVenue().getName());
    }

    @Test
    public void shouldDeleteEvent() {
        ereporitory.deleteById(event.getEventid());
        Optional<Event> found = ereporitory.findById(event.getEventid());
        assertFalse(found.isPresent());
    }

    @Test
    public void shouldNotFindNonExistingEvent() {
        Optional<Event> found = ereporitory.findById(9999L);
        assertFalse(found.isPresent());
    }
}
