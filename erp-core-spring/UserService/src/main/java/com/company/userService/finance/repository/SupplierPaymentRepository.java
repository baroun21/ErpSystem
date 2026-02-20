package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.SupplierPayment;
import com.company.erp.erp.entites.finance.Company;
import com.company.erp.erp.entites.finance.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
    Optional<SupplierPayment> findByPaymentNumber(String paymentNumber);
    List<SupplierPayment> findByCompany(Company company);
    List<SupplierPayment> findBySupplier(Supplier supplier);
    List<SupplierPayment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
}
