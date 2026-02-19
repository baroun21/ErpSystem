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
 * Payment entity
 * Unified payment structure for both customer and supplier payments
 */
@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_company", columnList = "company_id"),
    @Index(name = "idx_payment_date", columnList = "payment_date"),
    @Index(name = "idx_payment_status", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Column(name = "payment_number", nullable = false, length = 50)
    private String paymentNumber;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // Check, Wire, ACH, Credit Card, Cash

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "status", length = 50)
    private String status = "DRAFT"; // DRAFT, RECEIVED, CLEARED, CANCELLED

    @Column(name = "invoice_id")
    private Long invoiceId; // Link to invoice if applicable

    @Column(name = "bill_id")
    private Long billId; // Link to bill if applicable

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", foreignKey = @ForeignKey(name = "fk_payment_bank"))
    private BankAccount bankAccount;

    @Column(name = "cleared_date")
    private LocalDate clearedDate;

    public Payment() {
    }
}
