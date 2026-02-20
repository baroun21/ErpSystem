package com.company.erp.finance.application.service;

import com.company.erp.finance.domain.entity.JournalEntry;
import com.company.erp.finance.domain.entity.JournalEntryLine;
import com.company.erp.finance.domain.event.JournalEntryPostedEvent;
import com.company.erp.finance.domain.repository.JournalEntryLineRepository;
import com.company.erp.finance.domain.repository.JournalEntryRepository;
import com.company.erp.shared.infrastructure.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * PostingService
 * Handles journal entry posting with balance validation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostingService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryLineRepository journalEntryLineRepository;
    private final DomainEventPublisher domainEventPublisher;

    /**
     * Post a journal entry
     * Rules:
     * - Journal must balance (debits == credits)
     * - All lines must be valid (either debit or credit, not both)
     * - Once posted, entry becomes immutable
     */
    @Transactional
    public JournalEntry postJournalEntry(Long companyId, Long journalEntryId, String postedBy) {
        JournalEntry entry = journalEntryRepository.findById(journalEntryId)
                .orElseThrow(() -> new IllegalArgumentException("Journal entry not found: " + journalEntryId));

        if (!entry.getCompanyId().equals(companyId)) {
            throw new IllegalArgumentException("Company mismatch");
        }

        if (!"DRAFT".equals(entry.getStatus())) {
            throw new IllegalStateException("Cannot post non-DRAFT entry");
        }

        // Get all lines for this entry
        List<JournalEntryLine> lines = journalEntryLineRepository.findByJournalEntryInCompany(companyId, journalEntryId);

        // Validate all lines
        validateLines(lines);

        // Calculate totals
        BigDecimal totalDebit = lines.stream()
                .map(JournalEntryLine::getDebit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = lines.stream()
                .map(JournalEntryLine::getCredit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Update entry totals
        entry.setTotalDebit(totalDebit);
        entry.setTotalCredit(totalCredit);

        // Validate balance
        if (!isBalanced(totalDebit, totalCredit)) {
            throw new IllegalStateException("Journal entry does not balance. Debits: " + totalDebit + ", Credits: " + totalCredit);
        }

        entry.setIsBalanced(true);
        entry.setStatus("POSTED");
        entry.setPostedDate(LocalDate.now());
        entry.setPostedBy(postedBy);

        JournalEntry posted = journalEntryRepository.save(entry);
        domainEventPublisher.publish(new JournalEntryPostedEvent(
            posted.getId(),
            posted.getCompanyId(),
            posted.getReference(),
            posted.getDescription(),
            posted.getTotalDebit(),
            posted.getTotalCredit(),
            posted.getPostedBy(),
            posted.getPostedDate() == null ? null : posted.getPostedDate().atStartOfDay()
        ));
        log.info("Journal entry posted: {} for company: {}", journalEntryId, companyId);
        return posted;
    }

    /**
     * Reverse a posted journal entry
     * Creates a new reversing entry with opposite debits/credits
     */
    @Transactional
    public JournalEntry reverseJournalEntry(Long companyId, Long journalEntryId, String reversedBy) {
        JournalEntry originalEntry = journalEntryRepository.findById(journalEntryId)
                .orElseThrow(() -> new IllegalArgumentException("Journal entry not found: " + journalEntryId));

        if (!"POSTED".equals(originalEntry.getStatus())) {
            throw new IllegalStateException("Can only reverse POSTED entries");
        }

        // Create reversing entry  
        JournalEntry reversingEntry = new JournalEntry();
        reversingEntry.setCompanyId(companyId);
        reversingEntry.setEntryDate(LocalDate.now());
        reversingEntry.setReference("REVERSAL OF " + originalEntry.getReference());
        reversingEntry.setDescription("Reversal of entry #" + journalEntryId);
        reversingEntry.setStatus("DRAFT");
        reversingEntry.setReversalOfId(journalEntryId);
        reversingEntry.setTotalDebit(originalEntry.getTotalCredit());
        reversingEntry.setTotalCredit(originalEntry.getTotalDebit());
        reversingEntry.setIsBalanced(true);

        JournalEntry savedReversing = journalEntryRepository.save(reversingEntry);

        // Copy lines with reversed amounts
        List<JournalEntryLine> originalLines = journalEntryLineRepository.findByJournalEntryInCompany(companyId, journalEntryId);
        for (JournalEntryLine originalLine : originalLines) {
            JournalEntryLine reversingLine = new JournalEntryLine();
            reversingLine.setCompanyId(companyId);
            reversingLine.setJournalEntry(savedReversing);
            reversingLine.setAccount(originalLine.getAccount());
            reversingLine.setDebit(originalLine.getCredit());
            reversingLine.setCredit(originalLine.getDebit());
            reversingLine.setDescription("Reversal: " + originalLine.getDescription());
            journalEntryLineRepository.save(reversingLine);
        }

        log.info("Journal entry reversed: {} with new entry: {} for company: {}", journalEntryId, savedReversing.getId(), companyId);
        return savedReversing;
    }

    /**
     * Validate that all lines have either debit or credit, not both
     */
    private void validateLines(List<JournalEntryLine> lines) {
        if (lines.isEmpty()) {
            throw new IllegalStateException("Journal entry must have at least one line");
        }

        for (JournalEntryLine line : lines) {
            if (!line.isValid()) {
                throw new IllegalStateException("Invalid line: " + line.getId() + ". Line must have either debit or credit, not both");
            }
        }
    }

    /**
     * Check if debits equal credits
     */
    private boolean isBalanced(BigDecimal debits, BigDecimal credits) {
        return debits.setScale(2, java.math.RoundingMode.HALF_UP)
                .equals(credits.setScale(2, java.math.RoundingMode.HALF_UP));
    }
}
