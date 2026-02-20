package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class JournalEntryPostedEvent extends DomainEvent {

    private String reference;
    private String description;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private String postedBy;
    private LocalDateTime postedAt;

    public JournalEntryPostedEvent(
        Long journalEntryId,
        Long companyId,
        String reference,
        String description,
        BigDecimal totalDebit,
        BigDecimal totalCredit,
        String postedBy,
        LocalDateTime postedAt
    ) {
        super(journalEntryId, "JournalEntry", companyId, postedBy);
        this.reference = reference;
        this.description = description;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.postedBy = postedBy;
        this.postedAt = postedAt;
    }
}
