package utn.tpFinal.UDEE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.Invoice;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Page<Invoice> findByPaidAndResidenceId(Boolean paid,Integer idResidence, Pageable pageable);

    Page<Invoice> findByPaidAndResidenceIdIn(Boolean paid, List<Integer> residenceIds,Pageable pageable);



    List<Invoice> findByClientDniAndEmissionDateBetween(Integer dniClient, Date from, Date to);
}
