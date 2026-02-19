package com.company.erp.finance.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ReconciliationService
 * Handles bank account reconciliation and payment matching
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReconciliationService {

    /**
     * Reconcile bank transactions with cleared payments
     * Marks transactions as CLEARED when matched to cleared payments
     */
    @Transactional
    public void reconcileBankTransactions(Long companyId, Long bankAccountId, LocalDate reconciliationDate) {
        log.info("Reconciling bank transactions for account: {} as of {}", bankAccountId, reconciliationDate);
        // Future: Mark unmatched transactions for investigation
        // Query all PENDING bank_transactions since last reconciliation
        // Query all CLEARED payments
        // Match by amount and date
        // Update bank_transactions set status = 'CLEARED', cleared_date = reconciliationDate
    }

    /**
     * Match payments to bank transactions
     * Links Payment records to BankTransaction records
     */
    @Transactional
    public void matchPaymentsToTransactions(Long companyId, Long paymentId, Long bankTransactionId) {
        log.info("Matching payment: {} to bank transaction: {}", paymentId, bankTransactionId);
        // Update payment set bank_transaction_id = :bankTransactionId, status = 'CLEARED'
        // Update bank_transaction set cleared_date = now(), status = 'CLEARED'
    }

    /**
     * Calculate reconciled balance
     * Cleared balance = Bank statement balance
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateReconciledBalance(Long companyId, Long bankAccountId, LocalDate asOfDate) {
        log.info("Calculating reconciled balance for account: {} as of {}", bankAccountId, asOfDate);
        // Future: Sum all bank_transactions with status = 'CLEARED' and cleared_date <= asOfDate
        // This represents the cleared balance per bank statement
        return BigDecimal.ZERO;
    }

    /**
     * Get unreconciled (outstanding) items
     * Items since last successful reconciliation
     */
    @Transactional(readOnly = true)
    public BigDecimal getOutstandingBalance(Long companyId, Long bankAccountId, LocalDate asOfDate) {
        log.info("Getting outstanding balance for account: {} as of {}", bankAccountId, asOfDate);
        // Future: Sum bank_transactions with status = 'PENDING' since last reconciliation date
        // This represents the float between book and bank
        return BigDecimal.ZERO;
    }
}
