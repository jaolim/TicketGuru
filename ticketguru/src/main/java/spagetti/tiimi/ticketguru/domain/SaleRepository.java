package spagetti.tiimi.ticketguru.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SaleRepository extends CrudRepository<Sale, Long>{

    List<Sale> findAllByOrderByTimeDesc();
    List<Sale> findAllByOrderByTimeAsc();

}
