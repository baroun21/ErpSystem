package com.company.erp.finance.application.service;

import com.company.erp.finance.domain.repository.JournalEntryLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * LedgerService
 * Handles account ledger queries and balance calculations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerService {

    private final JournalEntryLineRepository journalEntryLineRepository;

    /**
     * Get running balance for an account
     */
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(Long companyId, Long accountId) {
        BigDecimal totalDebits = journalEntryLineRepository.sumDebitsForEntry(companyId, accountId);
        BigDecimal totalCredits = journalEntryLineRepository.sumCreditsForEntry(companyId, accountId);

        if (totalDebits == null) totalDebits = BigDecimal.ZERO;
        if (totalCredits == null) totalCredits = BigDecimal.ZERO;

        return totalDebits.subtract(totalCredits);
    }

    /**
     * Get account balance as of a specific date
     */
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalanceAsOfDate(Long companyId, Long accountId, LocalDate asOfDate) {
        // This would typically query an aggregated_account_balance table for performance
        // For now, returning a placeholder
        log.info("Getting account balance as of {} for account: {} in company: {}", asOfDate, accountId, companyId);
        return BigDecimal.ZERO;
    }

    /**
     * Get account balance over a date range
     */
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalanceForPeriod(Long companyId, Long accountId, LocalDate fromDate, LocalDate toDate) {
        // Query aggregated balance table filtered by date range
        log.info("Getting account balance for period {} to {} for account: {} in company: {}", fromDate, toDate, accountId, companyId);
        return BigDecimal.ZERO;
    }
}
