package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.InvoiceLine;
import com.company.erp.erp.entites.finance.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {
    List<InvoiceLine> findByInvoice(Invoice invoice);
}
