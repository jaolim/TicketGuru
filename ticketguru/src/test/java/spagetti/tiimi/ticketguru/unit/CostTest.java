package spagetti.tiimi.ticketguru.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;


import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.Venue;

public class CostTest {

    @Test
    void shouldCreateCostWithCorrectValues() {
        Venue venue = new Venue("name", "address");
        Event event = new Event("Event", venue, LocalDateTime.now(), 10);
        TicketType ticketType = new TicketType("TicketTypeName");

        Cost cost = new Cost(ticketType, 2.5, event);

        assertEquals(2.5, cost.getPrice());
        assertEquals(ticketType, cost.getType());
        assertEquals(event, cost.getEvent());
    }

    @Test
    void shouldUpdatePrice() {
        Venue venue = new Venue("name", "address");
        Event event = new Event("Event", venue, LocalDateTime.now(), 10);
        TicketType ticketType = new TicketType("TicketTypeName");
        Cost cost = new Cost(ticketType, 4.0, event);

        cost.setPrice(9.99);

        assertEquals(9.99, cost.getPrice());
    }

    @Test
    void shouldThrowExceptionForNegativePrice() {
        Venue venue = new Venue("name", "address");
        Event event = new Event("Event", venue, LocalDateTime.now(), 10);
        TicketType ticket = new TicketType("Basic");

        assertThrows(IllegalArgumentException.class,
            () -> new Cost(ticket, -5.0, event)
        );
    }
}
