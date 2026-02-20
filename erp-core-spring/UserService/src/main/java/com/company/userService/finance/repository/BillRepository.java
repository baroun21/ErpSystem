package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.Bill;
import com.company.erp.erp.entites.finance.Company;
import com.company.erp.erp.entites.finance.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBillNumber(String billNumber);
    List<Bill> findByCompany(Company company);
    List<Bill> findBySupplier(Supplier supplier);
    List<Bill> findByCompanyAndStatus(Company company, String status);
    List<Bill> findByBillDateBetween(LocalDate startDate, LocalDate endDate);
}
