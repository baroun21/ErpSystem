package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    @Query("SELECT je FROM JournalEntry je WHERE je.companyId = :companyId ORDER BY je.entryDate DESC")
    List<JournalEntry> findAllByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT je FROM JournalEntry je WHERE je.companyId = :companyId AND je.status = :status ORDER BY je.entryDate DESC")
    List<JournalEntry> findByStatusInCompany(@Param("companyId") Long companyId, @Param("status") String status);

    @Query("SELECT je FROM JournalEntry je WHERE je.companyId = :companyId AND je.entryDate BETWEEN :startDate AND :endDate ORDER BY je.entryDate DESC")
    List<JournalEntry> findByDateRangeInCompany(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT je FROM JournalEntry je WHERE je.companyId = :companyId AND je.reference = :reference")
    Optional<JournalEntry> findByReferenceInCompany(@Param("companyId") Long companyId, @Param("reference") String reference);

    @Query("SELECT COUNT(je) FROM JournalEntry je WHERE je.companyId = :companyId AND je.status = 'POSTED'")
    long countPostedEntriesInCompany(@Param("companyId") Long companyId);
}
