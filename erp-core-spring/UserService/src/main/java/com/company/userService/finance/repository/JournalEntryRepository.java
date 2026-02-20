package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.JournalEntry;
import com.company.erp.erp.entites.finance.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByEntryNumber(String entryNumber);
    List<JournalEntry> findByCompany(Company company);
    List<JournalEntry> findByCompanyAndStatus(Company company, String status);
    List<JournalEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);
}
