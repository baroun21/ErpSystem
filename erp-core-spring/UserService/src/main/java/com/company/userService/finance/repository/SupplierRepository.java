package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.Supplier;
import com.company.erp.erp.entites.finance.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByCode(String code);
    List<Supplier> findByCompany(Company company);
    List<Supplier> findByCompanyAndIsActive(Company company, Boolean isActive);
}
