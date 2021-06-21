package utn.tpFinal.UDEE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.EnergyMeter;

import java.util.Optional;

@Repository
public interface EnergyMeterRepository extends JpaRepository<EnergyMeter, Integer> {

    Optional<EnergyMeter> findBySerialNumber(Integer serialNumber);

    Page<EnergyMeter> findAll(Specification<EnergyMeter> meterSpecification,Pageable pageable);
}
