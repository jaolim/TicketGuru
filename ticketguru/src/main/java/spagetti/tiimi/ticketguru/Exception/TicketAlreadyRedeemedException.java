package spagetti.tiimi.ticketguru.Exception;

public class TicketAlreadyRedeemedException extends RuntimeException {
    
    public TicketAlreadyRedeemedException(String message) {
        super(message);
    }

}
