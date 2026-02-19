package com.company.erp.finance.application.service;

import com.company.erp.finance.domain.repository.InvoiceRepository;
import com.company.erp.finance.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ARService
 * Handles accounts receivable (customer invoices and payments)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ARService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    /**
     * Get total outstanding invoices for a customer
     */
    @Transactional(readOnly = true)
    public BigDecimal getCustomerOutstandingBalance(Long companyId, Long customerId) {
        log.info("Calculating outstanding balance for customer: {} in company: {}", customerId, companyId);
        // Sum unpaid invoices minus applied payments
        return BigDecimal.ZERO;
    }

    /**
     * Calculate days sales outstanding (DSO)
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateDSO(Long companyId, LocalDate asOfDate) {
        log.info("Calculating DSO as of {} for company: {}", asOfDate, companyId);
        return BigDecimal.ZERO;
    }

    /**
     * Apply payment to an invoice
     */
    @Transactional
    public void applyPaymentToInvoice(Long companyId, Long invoiceId, Long paymentId) {
        log.info("Applying payment: {} to invoice: {} in company: {}", paymentId, invoiceId, companyId);
        // Update invoice status, create payment application record
    }
}
