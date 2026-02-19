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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Bank Transaction entity
 * Represents all bank transactions for reconciliation
 */
@Entity
@Table(name = "bank_transactions", indexes = {
    @Index(name = "idx_bt_company", columnList = "company_id"),
    @Index(name = "idx_bt_bank_account", columnList = "bank_account_id"),
    @Index(name = "idx_bt_date", columnList = "transaction_date"),
    @Index(name = "idx_bt_status", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@Builder
public class BankTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", foreignKey = @ForeignKey(name = "fk_bt_account"))
    private BankAccount bankAccount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transaction_type", length = 50)
    private String transactionType; // DEBIT, CREDIT, CHEQUE, TRANSFER, FEE, INTEREST

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "check_number", length = 50)
    private String checkNumber;

    @Column(name = "status", length = 50)
    private String status = "PENDING"; // PENDING, CLEARED, REVERSED

    @Column(name = "cleared_date")
    private LocalDate clearedDate;

    @Column(name = "payment_id")
    private Long paymentId; // Link to Payment if applicable

    public BankTransaction() {
    }
}
