package utn.tpFinal.UDEE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.MeterModel;

@Repository
public interface ModelRepository extends JpaRepository<MeterModel, Integer> {
}
