package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.ChartOfAccount;
import com.company.erp.erp.entites.finance.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount, Long> {
    Optional<ChartOfAccount> findByAccountCode(String accountCode);
    List<ChartOfAccount> findByCompany(Company company);
    List<ChartOfAccount> findByCompanyAndAccountType(Company company, String accountType);
    List<ChartOfAccount> findByCompanyAndIsActive(Company company, Boolean isActive);
}
