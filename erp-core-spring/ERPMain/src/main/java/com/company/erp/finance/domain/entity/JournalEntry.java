package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Journal Entry entity
 * Core accounting entry containing multiple debit/credit lines
 * Must balance (sum of debits == sum of credits)
 */
@Entity
@Table(name = "journal_entries", indexes = {
    @Index(name = "idx_je_company", columnList = "company_id"),
    @Index(name = "idx_je_date", columnList = "entry_date"),
    @Index(name = "idx_je_status", columnList = "status")
})
@Getter
@Setter
public class JournalEntry extends BaseEntity {

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "status", length = 50)
    private String status = "DRAFT"; // DRAFT, POSTED, REVERSED

    @Column(name = "total_debit", precision = 15, scale = 2)
    private BigDecimal totalDebit = BigDecimal.ZERO;

    @Column(name = "total_credit", precision = 15, scale = 2)
    private BigDecimal totalCredit = BigDecimal.ZERO;

    @Column(name = "is_balanced")
    private Boolean isBalanced = Boolean.FALSE;

    @Column(name = "posted_date")
    private LocalDate postedDate;

    @Column(name = "posted_by", length = 255)
    private String postedBy;

    @Column(name = "reversal_of_id")
    private Long reversalOfId; // Reference to original entry if this is a reversal

    public JournalEntry() {
    }
}
