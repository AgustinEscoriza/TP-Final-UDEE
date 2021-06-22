package utn.tpFinal.UDEE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.tpFinal.UDEE.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Page<Invoice> findByPaidFalseAndResidenceId(Integer idResidence, Pageable pageable);
}
