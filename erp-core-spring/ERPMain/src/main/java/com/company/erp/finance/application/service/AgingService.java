package com.company.erp.finance.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * AgingService
 * Handles AR and AP aging analysis
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgingService {

    /**
     * Calculate AR aging buckets for a customer
     * 0-30 days, 31-60 days, 61-90 days, 90+ days
     */
    @Transactional(readOnly = true)
    public ARAgingBuckets calculateCustomerAging(Long companyId, Long customerId, LocalDate asOfDate) {
        log.info("Calculating AR aging for customer: {} as of {} in company: {}", customerId, asOfDate, companyId);
        return ARAgingBuckets.builder().build();
    }

    /**
     * Calculate AP aging buckets for a supplier
     */
    @Transactional(readOnly = true)
    public APAgingBuckets calculateSupplierAging(Long companyId, Long supplierId, LocalDate asOfDate) {
        log.info("Calculating AP aging for supplier: {} as of {} in company: {}", supplierId, asOfDate, companyId);
        return APAgingBuckets.builder().build();
    }

    /**
     * Calculate overdue amounts
     */
    private long getDaysOverdue(LocalDate dueDate, LocalDate asOfDate) {
        return ChronoUnit.DAYS.between(dueDate, asOfDate);
    }

    /**
     * AR Aging Buckets DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class ARAgingBuckets {
        private BigDecimal current030 = BigDecimal.ZERO;
        private BigDecimal days3160 = BigDecimal.ZERO;
        private BigDecimal days6190 = BigDecimal.ZERO;
        private BigDecimal days90Plus = BigDecimal.ZERO;

        public BigDecimal getTotal() {
            return current030.add(days3160).add(days6190).add(days90Plus);
        }
    }

    /**
     * AP Aging Buckets DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class APAgingBuckets {
        private BigDecimal current030 = BigDecimal.ZERO;
        private BigDecimal days3160 = BigDecimal.ZERO;
        private BigDecimal days6190 = BigDecimal.ZERO;
        private BigDecimal days90Plus = BigDecimal.ZERO;

        public BigDecimal getTotal() {
            return current030.add(days3160).add(days6190).add(days90Plus);
        }
    }
}
