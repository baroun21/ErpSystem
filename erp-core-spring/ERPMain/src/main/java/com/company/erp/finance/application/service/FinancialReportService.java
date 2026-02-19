package com.company.erp.finance.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * FinancialReportService
 * Generates financial reports: Trial Balance, P&L, Balance Sheet, Cash Flow
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialReportService {

    /**
     * Generate Trial Balance report
     * Lists all accounts with debit/credit balances for validation
     */
    @Transactional(readOnly = true)
    public TrialBalanceReport generateTrialBalance(Long companyId, LocalDate asOfDate) {
        log.info("Generating Trial Balance for company: {} as of {}", companyId, asOfDate);
        return TrialBalanceReport.builder().build();
    }

    /**
     * Generate Profit & Loss (Income Statement) report
     * Revenue - Expenses = Net Income
     */
    @Transactional(readOnly = true)
    public ProfitLossReport generateProfitLoss(Long companyId, LocalDate fromDate, LocalDate toDate) {
        log.info("Generating P&L for company: {} from {} to {}", companyId, fromDate, toDate);
        return ProfitLossReport.builder().build();
    }

    /**
     * Generate Balance Sheet report
     * Assets = Liabilities + Equity
     */
    @Transactional(readOnly = true)
    public BalanceSheetReport generateBalanceSheet(Long companyId, LocalDate asOfDate) {
        log.info("Generating Balance Sheet for company: {} as of {}", companyId, asOfDate);
        return BalanceSheetReport.builder().build();
    }

    /**
     * Generate Cash Flow report (basic)
     * Operating, Investing, Financing activities
     */
    @Transactional(readOnly = true)
    public CashFlowReport generateCashFlow(Long companyId, LocalDate fromDate, LocalDate toDate) {
        log.info("Generating Cash Flow for company: {} from {} to {}", companyId, fromDate, toDate);
        return CashFlowReport.builder().build();
    }

    @lombok.Data
    @lombok.Builder
    public static class TrialBalanceReport {
        private LocalDate reportDate;
        private BigDecimal totalDebits = BigDecimal.ZERO;
        private BigDecimal totalCredits = BigDecimal.ZERO;
    }

    @lombok.Data
    @lombok.Builder
    public static class ProfitLossReport {
        private LocalDate fromDate;
        private LocalDate toDate;
        private BigDecimal totalRevenue = BigDecimal.ZERO;
        private BigDecimal totalExpenses = BigDecimal.ZERO;
        private BigDecimal netIncome = BigDecimal.ZERO;
    }

    @lombok.Data
    @lombok.Builder
    public static class BalanceSheetReport {
        private LocalDate asOfDate;
        private BigDecimal totalAssets = BigDecimal.ZERO;
        private BigDecimal totalLiabilities = BigDecimal.ZERO;
        private BigDecimal totalEquity = BigDecimal.ZERO;
    }

    @lombok.Data
    @lombok.Builder
    public static class CashFlowReport {
        private LocalDate fromDate;
        private LocalDate toDate;
        private BigDecimal operatingCashFlow = BigDecimal.ZERO;
        private BigDecimal investingCashFlow = BigDecimal.ZERO;
        private BigDecimal financingCashFlow = BigDecimal.ZERO;
        private BigDecimal netCashFlow = BigDecimal.ZERO;
    }
}
