package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.*;

import java.time.LocalDateTime;

/**
 * JournalReversedEvent
 * Fired when a posted journal entry is reversed via a new reversing entry
 * Triggers: Reversal audit log, account balance update (undoing original entry)
 */
@Getter
@Setter
@NoArgsConstructor
public class JournalReversedEvent extends DomainEvent {

    private Long reversingJournalEntryId;
    private String reason; // CORRECTION, DATA_ERROR, POSTED_IN_ERROR, etc.
    private String reversalReason;
    private String reversedBy;
    private LocalDateTime reversalDate;

    public JournalReversedEvent(
        Long originalJournalEntryId,
        Long reversingJournalEntryId,
        Long companyId,
        String reason,
        String reversalReason,
        String reversedBy,
        LocalDateTime reversalDate
    ) {
        super(originalJournalEntryId, "JournalEntry", companyId, reversedBy);
        this.reversingJournalEntryId = reversingJournalEntryId;
        this.reason = reason;
        this.reversalReason = reversalReason;
        this.reversedBy = reversedBy;
        this.reversalDate = reversalDate;
    }
}
