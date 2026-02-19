package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i WHERE i.companyId = :companyId AND i.invoiceNumber = :number")
    Optional<Invoice> findByNumberInCompany(@Param("companyId") Long companyId, @Param("number") String number);

    @Query("SELECT i FROM Invoice i WHERE i.companyId = :companyId ORDER BY i.invoiceDate DESC")
    List<Invoice> findAllInCompany(@Param("companyId") Long companyId);
}
