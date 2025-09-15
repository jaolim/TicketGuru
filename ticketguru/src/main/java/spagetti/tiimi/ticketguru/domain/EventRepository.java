package spagetti.tiimi.ticketguru.domain;

import org.springframework.data.repository.CrudRepository;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public interface EventRepository extends CrudRepository<Event, Long>{

}
