package utn.tpFinal.UDEE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.Measurement;

import java.util.Date;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    /*@Query(value = "SELECT * FROM measurements  WHERE  id = :idResidence AND date BETWEEN :from AND :to", nativeQuery = true)*/
    Page<Measurement> findByResidenceIdAndDateBetween(Integer idResidence, Date from, Date to, Pageable pageable);

    Page<Measurement> findByResidenceIdInAndDateBetween(List<Integer> residencesIds, Date from, Date to, Pageable pageable);
}
