package com.company.erp.finance.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * APService
 * Accounts Payable operations management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class APService {

    /**
     * Calculate supplier's outstanding balance
     * Sum of unpaid bills minus payments applied
     */
    @Transactional(readOnly = true)
    public BigDecimal getSupplierOutstandingBalance(Long companyId, Long supplierId, LocalDate asOfDate) {
        log.info("Calculating outstanding balance for supplier: {} as of {}", supplierId, asOfDate);
        // Future: Query bills where supplier_id = :supplierId and status != 'PAID'
        // Sum bill_totals - sum payments_applied
        // If bill has payment_applications, subtract from total
        return BigDecimal.ZERO;
    }

    /**
     * Calculate Days Payable Outstanding (DPO)
     * Average days from bill receipt to payment
     * DPO = (Average Accounts Payable) / (Cost of Goods Sold / Days in Period) * Days in Period
     * Simplified: DPO = (Total Outstanding AP / Average Daily Bill Amount)
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateDPO(Long companyId, LocalDate fromDate, LocalDate toDate) {
        log.info("Calculating DPO for company: {} from {} to {}", companyId, fromDate, toDate);
        // Future: Query bills in period
        // Calculate average bill amount per day
        // Use current outstanding balance
        // DPO = outstanding_balance / (total_bills_period / days_in_period)
        return BigDecimal.ZERO;
    }

    /**
     * Apply payment to bill
     * Updates bill status and creates payment application record
     */
    @Transactional
    public void applyPaymentToBill(Long companyId, Long billId, Long paymentId, BigDecimal amountApplied) {
        log.info("Applying payment: {} to bill: {} amount: {}", paymentId, billId, amountApplied);
        // Future: Create payment_application record
        // Update bill: remaining_balance -= amountApplied
        // If remaining_balance <= 0, update bill status = 'PAID'
        // Publish PaymentAppliedEvent for accounting entries
    }

    /**
     * Calculate average payment period
     * Historical trend: days from bill date to payment date
     */
    @Transactional(readOnly = true)
    public long getAveragePaymentDays(Long companyId, LocalDate fromDate, LocalDate toDate) {
        log.info("Calculating average payment days for company: {} from {} to {}", companyId, fromDate, toDate);
        // Future: Query paid bills in period where status = 'PAID' and paid_date is set
        // Calculate payment_date - bill_date for each
        // Return average
        return 0L;
    }
}
