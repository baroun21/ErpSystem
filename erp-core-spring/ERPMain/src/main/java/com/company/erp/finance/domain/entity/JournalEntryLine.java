package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Journal Entry Line Item
 * Individual debit or credit line within a journal entry
 */
@Entity
@Table(name = "journal_entry_lines", indexes = {
    @Index(name = "idx_jel_company", columnList = "company_id"),
    @Index(name = "idx_jel_entry", columnList = "journal_entry_id"),
    @Index(name = "idx_jel_account", columnList = "account_id")
})
@Getter
@Setter
public class JournalEntryLine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", foreignKey = @ForeignKey(name = "fk_jel_entry"))
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "fk_jel_account"))
    private ChartOfAccount account;

    @Column(name = "debit", precision = 15, scale = 2)
    private BigDecimal debit = BigDecimal.ZERO;

    @Column(name = "credit", precision = 15, scale = 2)
    private BigDecimal credit = BigDecimal.ZERO;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "cost_center_id")
    private Long costCenterId; // Optional allocation to cost center

    public JournalEntryLine() {
    }

    /**
     * Validates that line has either debit or credit, not both
     */
    public boolean isValid() {
        boolean hasDebit = debit != null && debit.compareTo(BigDecimal.ZERO) > 0;
        boolean hasCredit = credit != null && credit.compareTo(BigDecimal.ZERO) > 0;
        return (hasDebit && !hasCredit) || (!hasDebit && hasCredit);
    }
}
