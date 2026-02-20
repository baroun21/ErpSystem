package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.Invoice;
import com.company.erp.erp.entites.finance.Company;
import com.company.erp.erp.entites.finance.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByCompany(Company company);
    List<Invoice> findByCustomer(Customer customer);
    List<Invoice> findByCompanyAndStatus(Company company, String status);
    List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
}
