package spagetti.tiimi.ticketguru.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;


import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.TicketType;

public class CostTest {

    // TODO Toteutettavia testejä
    // hinnan laskeminen oikein

    @Test
    void shouldCreateCostWithCorrectValues() {
        Event event = new Event("Event", "Venue", LocalDateTime.now());
        TicketType ticketType = new TicketType("TicketTypeName");

        Cost cost = new Cost(ticketType, 2.5, event);

        assertEquals(2.5, cost.getPrice());
        assertEquals(ticketType, cost.getType());
        assertEquals(event, cost.getEvent());
    }

    @Test
    void shouldUpdatePrice() {
        Event event = new Event("Event", "Venue", LocalDateTime.now());
        TicketType ticketType = new TicketType("TicketTypeName");
        Cost cost = new Cost(ticketType, 4.0, event);

        cost.setPrice(9.99);

        assertEquals(9.99, cost.getPrice());
    }

    @Test
    void shouldThrowExceptionForNegativePrice() {
        Event event = new Event("Event", "Venue", LocalDateTime.now());
        TicketType ticket = new TicketType("Basic");

        assertThrows(IllegalArgumentException.class,
            () -> new Cost(ticket, -5.0, event)
        );
    }
}
