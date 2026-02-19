package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.JournalEntryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {

    @Query("SELECT jel FROM JournalEntryLine jel WHERE jel.companyId = :companyId AND jel.journalEntry.id = :journalEntryId")
    List<JournalEntryLine> findByJournalEntryInCompany(@Param("companyId") Long companyId, @Param("journalEntryId") Long journalEntryId);

    @Query("SELECT jel FROM JournalEntryLine jel WHERE jel.companyId = :companyId AND jel.account.id = :accountId ORDER BY jel.createdAt DESC")
    List<JournalEntryLine> findByAccountInCompany(@Param("companyId") Long companyId, @Param("accountId") Long accountId);

    @Query("SELECT jel FROM JournalEntryLine jel WHERE jel.companyId = :companyId AND jel.costCenterId = :costCenterId")
    List<JournalEntryLine> findByCostCenterInCompany(@Param("companyId") Long companyId, @Param("costCenterId") Long costCenterId);

    @Query("SELECT SUM(jel.debit) FROM JournalEntryLine jel WHERE jel.companyId = :companyId AND jel.journalEntry.id = :journalEntryId")
    java.math.BigDecimal sumDebitsForEntry(@Param("companyId") Long companyId, @Param("journalEntryId") Long journalEntryId);

    @Query("SELECT SUM(jel.credit) FROM JournalEntryLine jel WHERE jel.companyId = :companyId AND jel.journalEntry.id = :journalEntryId")
    java.math.BigDecimal sumCreditsForEntry(@Param("companyId") Long companyId, @Param("journalEntryId") Long journalEntryId);
}
