package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.JournalEntryLine;
import com.company.erp.erp.entites.finance.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {
    List<JournalEntryLine> findByJournalEntry(JournalEntry journalEntry);
    
    /**
     * Find all lines for a journal entry in a specific company
     * This method filters by company through the journal entry relationship
     */
    default List<JournalEntryLine> findByJournalEntryInCompany(Long companyId, Long journalEntryId) {
        // This is a helper method - in a real scenario, you'd want a native query
        // For now, we'll use the standard findByJournalEntry and filter programmatically
        return findByJournalEntry_IdAndJournalEntry_Company_Id(journalEntryId, companyId);
    }
    
    List<JournalEntryLine> findByJournalEntry_IdAndJournalEntry_Company_Id(Long journalEntryId, Long companyId);
}
