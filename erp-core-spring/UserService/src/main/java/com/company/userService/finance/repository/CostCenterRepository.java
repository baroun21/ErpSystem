package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.CostCenter;
import com.company.erp.erp.entites.finance.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Long> {
    Optional<CostCenter> findByCode(String code);
    List<CostCenter> findByCompany(Company company);
    List<CostCenter> findByCompanyAndIsActive(Company company, Boolean isActive);
}
