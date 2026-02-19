package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("SELECT ba FROM BankAccount ba WHERE ba.companyId = :companyId")
    List<BankAccount> findAllByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT ba FROM BankAccount ba WHERE ba.companyId = :companyId AND ba.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumberInCompany(@Param("companyId") Long companyId, @Param("accountNumber") String accountNumber);

    @Query("SELECT ba FROM BankAccount ba WHERE ba.companyId = :companyId AND ba.active = true")
    List<BankAccount> findActiveAccountsInCompany(@Param("companyId") Long companyId);

    @Query("SELECT ba FROM BankAccount ba WHERE ba.companyId = :companyId AND ba.currency = :currency")
    List<BankAccount> findByCurrencyInCompany(@Param("companyId") Long companyId, @Param("currency") String currency);
}
