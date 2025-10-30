package spagetti.tiimi.ticketguru.domain;

import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long>{

    Ticket findByTicketCode(String ticketcode);
    Boolean existsByTicketCode(String ticketcode);

}