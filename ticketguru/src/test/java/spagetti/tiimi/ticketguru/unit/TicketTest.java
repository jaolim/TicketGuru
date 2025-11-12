package spagetti.tiimi.ticketguru.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.Ticket;

public class TicketTest {

    @Test
    void shouldCreateTicketWithCostOnly() {
        Cost cost = new Cost();
        Ticket ticket = new Ticket(cost);

        assertEquals(cost, ticket.getCost());
        assertNull(ticket.getSale());
        assertFalse(ticket.getRedeemed());
        assertNull(ticket.getRedeemedTime());
        assertNull(ticket.getTicketCode());
    }

    @Test
    void shouldCreateTicketWithCostAndSale() {
        Cost cost = new Cost();
        Sale sale = new Sale();
        Ticket ticket = new Ticket(cost, sale);

        assertEquals(cost, ticket.getCost());
        assertEquals(sale, ticket.getSale());
        assertFalse(ticket.getRedeemed());
    }

    @Test
    void shouldSetAndGetFields() {
        Ticket ticket = new Ticket();
        Cost cost = new Cost();
        Sale sale = new Sale();
        LocalDateTime time = LocalDateTime.now();

        ticket.setCost(cost);
        ticket.setSale(sale);
        ticket.setRedeemed(true);
        ticket.setRedeemedTime(time);
        ticket.setTicketCode("ABC123");

        assertEquals(cost, ticket.getCost());
        assertEquals(sale, ticket.getSale());
        assertTrue(ticket.getRedeemed());
        assertEquals(time, ticket.getRedeemedTime());
        assertEquals("ABC123", ticket.getTicketCode());
    }

    @Test
    void toStringShouldContainCostAndRedeemed() {
        Cost cost = new Cost();
        Ticket ticket = new Ticket(cost);

        String str = ticket.toString();
        assertTrue(str.contains("Redeemed"));
        assertTrue(str.contains(cost.toString()));
    }
    
}
