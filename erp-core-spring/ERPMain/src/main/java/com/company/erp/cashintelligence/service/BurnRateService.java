package com.company.erp.cashintelligence.service;

import com.company.erp.erp.Dtos.cash.BurnRateDTO;
import com.company.erp.finance.domain.entity.Payment;
import com.company.erp.finance.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BurnRateService {

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public BurnRateDTO calculateBurnRate(Long companyId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(90);
        List<Payment> payments = paymentRepository.findByDateRangeInCompany(companyId, start, end);

        BigDecimal inflow = BigDecimal.ZERO;
        BigDecimal outflow = BigDecimal.ZERO;

        for (Payment payment : payments) {
            BigDecimal amount = safe(payment.getAmount());
            if (payment.getInvoiceId() != null) {
                inflow = inflow.add(amount);
            } else if (payment.getBillId() != null) {
                outflow = outflow.add(amount);
            }
        }

        long days = Math.max(1, ChronoUnit.DAYS.between(start, end));
        BigDecimal months = BigDecimal.valueOf(days).divide(BigDecimal.valueOf(30), 4, java.math.RoundingMode.HALF_UP);
        BigDecimal netMonthly = inflow.subtract(outflow).divide(months, 2, java.math.RoundingMode.HALF_UP);
        BigDecimal burnRate = netMonthly.signum() < 0 ? netMonthly.abs() : BigDecimal.ZERO;

        return BurnRateDTO.builder()
            .periodStart(start)
            .periodEnd(end)
            .netCashFlowMonthly(netMonthly)
            .burnRateMonthly(burnRate)
            .build();
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
