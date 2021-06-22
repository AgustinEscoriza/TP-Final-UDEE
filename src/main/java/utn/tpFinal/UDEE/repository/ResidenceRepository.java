package utn.tpFinal.UDEE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.model.Residence;

import java.util.Date;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Integer> {
    Page<Residence> findAll(Specification<Residence> residenceSpecification, Pageable pageable);
}
