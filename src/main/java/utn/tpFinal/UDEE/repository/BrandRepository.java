package utn.tpFinal.UDEE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.Brand;
import utn.tpFinal.UDEE.model.EnergyMeter;

import java.util.Optional;


@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Optional<EnergyMeter> getBrandById(String serialNumber);
}
