package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * BalanceSheetReport
 * Statement of financial position: Assets = Liabilities + Equity as of a date
 */
@Entity
@Table(name = "balance_sheet_reports", indexes = {
        @Index(name = "idx_bsr_company_date", columnList = "company_id, report_date"),
        @Index(name = "idx_bsr_report_date", columnList = "report_date")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceSheetReport extends BaseEntity {

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate reportDate;

    // Assets
    @Column(precision = 15, scale = 2)
    private BigDecimal currentAssets; // Cash, AR, Inventory

    @Column(precision = 15, scale = 2)
    private BigDecimal fixedAssets; // PP&E net of depreciation

    @Column(precision = 15, scale = 2)
    private BigDecimal otherAssets; // Intangibles, long-term receivables

    @Column(precision = 15, scale = 2)
    private BigDecimal totalAssets; // Current + Fixed + Other

    // Liabilities
    @Column(precision = 15, scale = 2)
    private BigDecimal currentLiabilities; // AP, ST debt, accrued expenses

    @Column(precision = 15, scale = 2)
    private BigDecimal longtermLiabilities; // LT debt, deferred taxes

    @Column(precision = 15, scale = 2)
    private BigDecimal totalLiabilities; // Current + LT

    // Equity
    @Column(precision = 15, scale = 2)
    private BigDecimal commonStock; // Paid-in capital

    @Column(precision = 15, scale = 2)
    private BigDecimal retainedEarnings; // Cumulative net income

    @Column(precision = 15, scale = 2)
    private BigDecimal totalEquity; // Stock + RE

    @Column(columnDefinition = "NVARCHAR2(2000)")
    private String notes;

    public boolean isBalanced() {
        return totalAssets.compareTo(
                totalLiabilities.add(totalEquity)
        ) == 0;
    }
}
