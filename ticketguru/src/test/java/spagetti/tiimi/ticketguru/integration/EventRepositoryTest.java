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

@SpringBootTest
public class EventRepositoryTest {
        
    @Autowired
    private EventRepository ereporitory;

    private Event event;

    @BeforeEach
    public void setup() {
        event = new Event("Test Event", "Test Venue", LocalDateTime.now());
        ereporitory.save(event);
    }

    @AfterEach
    public void cleanup() {
        ereporitory.deleteAll();
    }

    @Test
    public void shouldCreateNewEvent() {
        Event newEvent = new Event("Concert", "Main Hall", LocalDateTime.now().plusDays(1));
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
        event.setVenue("Updated Venue");
        ereporitory.save(event);
        Optional<Event> found = ereporitory.findById(event.getEventid());
        assertTrue(found.isPresent());
        assertEquals("Updated Venue", found.get().getVenue());
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
