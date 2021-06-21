package utn.tpFinal.UDEE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
