package spagetti.tiimi.ticketguru.domain;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CostRepository extends CrudRepository<Cost, Long>{
    List<Cost> findByEvent(Event event);

}
