package utn.tpFinal.UDEE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.model.proyection.Top10Clients;

import java.util.Date;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Page<Client> findAll(Specification<Client> clientSpecification, Pageable pageable);

    @Query(value = "CALL getTop10Clients(:from, :to)", nativeQuery = true)
    List<Top10Clients> getTop10Clients(Date from, Date to);
}
