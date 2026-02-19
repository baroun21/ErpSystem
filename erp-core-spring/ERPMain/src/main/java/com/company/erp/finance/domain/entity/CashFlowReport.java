package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CashFlowReport
 * Statement of cash flows: Operating, Investing, Financing activities
 */
@Entity
@Table(name = "cash_flow_reports", indexes = {
        @Index(name = "idx_cfr_company_period", columnList = "company_id, from_date, to_date"),
        @Index(name = "idx_cfr_period", columnList = "from_date, to_date")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashFlowReport extends BaseEntity {

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate fromDate;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate toDate;

    // Operating Activities
    @Column(precision = 15, scale = 2)
    private BigDecimal netIncome; // Starting point (indirect method)

    @Column(precision = 15, scale = 2)
    private BigDecimal depreciation; // Add back non-cash

    @Column(precision = 15, scale = 2)
    private BigDecimal amortization; // Add back non-cash

    @Column(precision = 15, scale = 2)
    private BigDecimal changeInWorkingCapital; // AR, AP, Inventory changes

    @Column(precision = 15, scale = 2)
    private BigDecimal operatingCashFlow; // Net from operations

    // Investing Activities
    @Column(precision = 15, scale = 2)
    private BigDecimal capitalExpenditures; // PP&E purchases

    @Column(precision = 15, scale = 2)
    private BigDecimal investmentPurchases; // Securities, etc.

    @Column(precision = 15, scale = 2)
    private BigDecimal investingCashFlow; // Net from investing (negative)

    // Financing Activities
    @Column(precision = 15, scale = 2)
    private BigDecimal debtProceeds; // New loans/bonds issued

    @Column(precision = 15, scale = 2)
    private BigDecimal debtRepayment; // Loan principal payments

    @Column(precision = 15, scale = 2)
    private BigDecimal dividendsPaid; // Distribution to shareholders

    @Column(precision = 15, scale = 2)
    private BigDecimal financingCashFlow; // Net from financing

    // Summary
    @Column(precision = 15, scale = 2)
    private BigDecimal netCashFlow; // Operating + Investing + Financing

    @Column(columnDefinition = "NVARCHAR2(2000)")
    private String notes;
}
