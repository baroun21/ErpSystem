package com.company.erp.erp.entites.finance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_entry_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private ChartOfAccount account;

    @Column(name = "line_number")
    private Integer lineNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "debit_amount", precision = 15, scale = 2)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(name = "credit_amount", precision = 15, scale = 2)
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;

    @Column(name = "memo")
    private String memo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Alias for debitAmount (convenience getter)
     */
    public BigDecimal getDebit() {
        return debitAmount != null ? debitAmount : BigDecimal.ZERO;
    }

    /**
     * Alias for debitAmount (convenience setter)
     */
    public void setDebit(BigDecimal amount) {
        this.debitAmount = amount;
    }

    /**
     * Alias for creditAmount (convenience getter)
     */
    public BigDecimal getCredit() {
        return creditAmount != null ? creditAmount : BigDecimal.ZERO;
    }

    /**
     * Alias for creditAmount (convenience setter)
     */
    public void setCredit(BigDecimal amount) {
        this.creditAmount = amount;
    }

    /**
     * Check if line is valid (has either debit or credit, but not both)
     */
    public boolean isValid() {
        BigDecimal debit = getDebit();
        BigDecimal credit = getCredit();
        BigDecimal zero = BigDecimal.ZERO;
        
        // Must have either debit or credit, not both, not neither
        boolean hasDebit = debit.compareTo(zero) > 0;
        boolean hasCredit = credit.compareTo(zero) > 0;
        
        return (hasDebit && !hasCredit) || (!hasDebit && hasCredit);
    }
}
