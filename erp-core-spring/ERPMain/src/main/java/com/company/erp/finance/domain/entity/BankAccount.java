package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Bank Account entity
 * Represents company bank accounts for transaction tracking and reconciliation
 */
@Entity
@Table(name = "bank_accounts", indexes = {
    @Index(name = "idx_ba_company", columnList = "company_id"),
    @Index(name = "idx_ba_number", columnList = "account_number")
})
@Getter
@Setter
@AllArgsConstructor
@Builder
public class BankAccount extends BaseEntity {

    @Column(name = "account_number", length = 50, nullable = false)
    private String accountNumber;

    @Column(name = "bank_name", length = 255)
    private String bankName;

    @Column(name = "account_holder", length = 255)
    private String accountHolder;

    @Column(name = "account_type", length = 50)
    private String accountType; // Checking, Savings, Money Market, etc.

    @Column(name = "currency", length = 3)
    private String currency = "USD";

    @Column(name = "current_balance", precision = 15, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(name = "reconciled_balance", precision = 15, scale = 2)
    private BigDecimal reconciledBalance = BigDecimal.ZERO;

    @Column(name = "routing_number", length = 50)
    private String routingNumber;

    @Column(name = "swift_code", length = 50)
    private String swiftCode;

    @Column(name = "iban", length = 50)
    private String iban;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    public BankAccount() {
    }
}
