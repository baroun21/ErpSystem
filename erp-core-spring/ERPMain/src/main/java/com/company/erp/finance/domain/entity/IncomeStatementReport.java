package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * IncomeStatementReport
 * Profit & Loss statement showing revenue, expenses, and net income for period
 */
@Entity
@Table(name = "income_statement_reports", indexes = {
        @Index(name = "idx_isr_company_period", columnList = "company_id, from_date, to_date"),
        @Index(name = "idx_isr_period", columnList = "from_date, to_date")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeStatementReport extends BaseEntity {

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate fromDate;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate toDate;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalRevenue; // All revenue accounts

    @Column(precision = 15, scale = 2)
    private BigDecimal costOfGoodsSold; // COGS

    @Column(precision = 15, scale = 2)
    private BigDecimal grossProfit; // Revenue - COGS

    @Column(precision = 15, scale = 2)
    private BigDecimal operatingExpenses; // SG&A, R&D, etc.

    @Column(precision = 15, scale = 2)
    private BigDecimal operatingIncome; // Gross Profit - OpEx

    @Column(precision = 15, scale = 2)
    private BigDecimal interestExpense;

    @Column(precision = 15, scale = 2)
    private BigDecimal otherIncome; // Investment income, etc.

    @Column(precision = 15, scale = 2)
    private BigDecimal incomeBeforeTax; // Op Income - Interest + Other

    @Column(precision = 15, scale = 2)
    private BigDecimal incomeTaxExpense;

    @Column(precision = 15, scale = 2)
    private BigDecimal netIncome; // Income Before Tax - Tax

    @Column(columnDefinition = "NVARCHAR2(2000)")
    private String notes;
}
