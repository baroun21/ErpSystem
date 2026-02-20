package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.BankAccount;
import com.company.erp.erp.entites.finance.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    List<BankAccount> findByCompany(Company company);
    List<BankAccount> findByCompanyAndIsActive(Company company, Boolean isActive);
}
