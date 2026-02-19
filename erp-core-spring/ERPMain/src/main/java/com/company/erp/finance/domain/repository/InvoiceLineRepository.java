package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {

    @Query("SELECT il FROM InvoiceLine il WHERE il.companyId = :companyId AND il.invoice.id = :invoiceId ORDER BY il.lineNumber")
    List<InvoiceLine> findByInvoiceInCompany(@Param("companyId") Long companyId, @Param("invoiceId") Long invoiceId);

    @Query("SELECT SUM(il.lineTotal) FROM InvoiceLine il WHERE il.companyId = :companyId AND il.invoice.id = :invoiceId")
    java.math.BigDecimal sumLineTotalsForInvoice(@Param("companyId") Long companyId, @Param("invoiceId") Long invoiceId);

    @Query("SELECT il FROM InvoiceLine il WHERE il.companyId = :companyId AND il.costCenterId = :costCenterId")
    List<InvoiceLine> findByCostCenterInCompany(@Param("companyId") Long companyId, @Param("costCenterId") Long costCenterId);
}
