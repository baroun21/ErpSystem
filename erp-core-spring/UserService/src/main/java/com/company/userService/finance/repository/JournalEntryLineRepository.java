package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.JournalEntryLine;
import com.company.erp.erp.entites.finance.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {
    List<JournalEntryLine> findByJournalEntry(JournalEntry journalEntry);
}
