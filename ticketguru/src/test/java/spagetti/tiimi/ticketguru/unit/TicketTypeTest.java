package spagetti.tiimi.ticketguru.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.TicketType;

public class TicketTypeTest {

    @Test
    void shouldCreateTicketTypeWithName() {
        TicketType type = new TicketType("VIP");
        assertEquals("VIP", type.getName());
        assertNull(type.getNote());
        assertNull(type.getCosts());
    }

    @Test
    void shouldCreateTicketTypeWithNameAndNote() {
        TicketType type = new TicketType("Standard", "Some note");
        assertEquals("Standard", type.getName());
        assertEquals("Some note", type.getNote());
    }

    @Test
    void shouldSetAndGetFields() {
        TicketType type = new TicketType();
        type.setName("Premium");
        type.setNote("Note");
        
        List<Cost> costs = new ArrayList<>();
        type.setCosts(costs);
        
        assertEquals("Premium", type.getName());
        assertEquals("Note", type.getNote());
        assertEquals(costs, type.getCosts());
    }

    @Test
    void toStringShouldReturnName() {
        TicketType type = new TicketType("VIP");
        assertEquals("VIP", type.toString());
    }
    
}
