package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JournalPostedEvent
 * Fired when a journal entry transitions from DRAFT to POSTED status
 * Triggers: Audit log creation, account balance update
 */
@Getter
@Setter
@NoArgsConstructor
public class JournalPostedEvent extends DomainEvent {

    private String reference;
    private String description;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private String postedBy;
    private LocalDateTime postedDate;

    public JournalPostedEvent(
        Long journalEntryId,
        Long companyId,
        String reference,
        String description,
        BigDecimal totalDebit,
        BigDecimal totalCredit,
        String postedBy,
        LocalDateTime postedDate
    ) {
        super(journalEntryId, "JournalEntry", companyId, postedBy);
        this.reference = reference;
        this.description = description;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.postedBy = postedBy;
        this.postedDate = postedDate;
    }
}
