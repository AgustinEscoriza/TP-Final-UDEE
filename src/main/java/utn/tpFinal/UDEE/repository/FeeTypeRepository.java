package utn.tpFinal.UDEE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.FeeType;

@Repository
public interface FeeTypeRepository extends JpaRepository<FeeType,Integer> {
}
