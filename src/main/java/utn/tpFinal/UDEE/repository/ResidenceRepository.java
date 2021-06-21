package utn.tpFinal.UDEE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.Residence;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Integer> {
}
