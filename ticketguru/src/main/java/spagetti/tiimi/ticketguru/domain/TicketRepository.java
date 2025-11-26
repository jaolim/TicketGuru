package spagetti.tiimi.ticketguru.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    Ticket findByTicketCode(String ticketcode);

    Boolean existsByTicketCode(String ticketcode);

    List<Ticket> findBySale(Sale sale);

    Long countByCost_Costid(Long costid);

    List<Ticket> findAll();

    Long countByCost_Event_Eventid(Long eventid);

}