package spagetti.tiimi.ticketguru.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long>{

    List<Event> findDistinctByCosts_Tickets_Sale_Saleid(Long saleid);

}
