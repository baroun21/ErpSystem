package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    @Query("SELECT bt FROM BankTransaction bt WHERE bt.companyId = :companyId AND bt.bankAccount.id = :bankAccountId ORDER BY bt.transactionDate DESC")
    List<BankTransaction> findByBankAccountInCompany(@Param("companyId") Long companyId, @Param("bankAccountId") Long bankAccountId);

    @Query("SELECT bt FROM BankTransaction bt WHERE bt.companyId = :companyId AND bt.transactionDate BETWEEN :startDate AND :endDate ORDER BY bt.transactionDate DESC")
    List<BankTransaction> findByDateRangeInCompany(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT bt FROM BankTransaction bt WHERE bt.companyId = :companyId AND bt.status = :status")
    List<BankTransaction> findByStatusInCompany(@Param("companyId") Long companyId, @Param("status") String status);

    @Query("SELECT bt FROM BankTransaction bt WHERE bt.companyId = :companyId AND bt.reference = :reference")
    List<BankTransaction> findByReferenceInCompany(@Param("companyId") Long companyId, @Param("reference") String reference);

    @Query("SELECT SUM(bt.amount) FROM BankTransaction bt WHERE bt.companyId = :companyId AND bt.bankAccount.id = :bankAccountId AND bt.status = 'CLEARED' AND bt.transactionDate <= :asOfDate")
    java.math.BigDecimal calculateClearedBalance(@Param("companyId") Long companyId, @Param("bankAccountId") Long bankAccountId, @Param("asOfDate") LocalDate asOfDate);
}
