package com.company.erp.cashintelligence.service;

import com.company.erp.erp.Dtos.cash.CashForecastDTO;
import com.company.erp.finance.domain.entity.Bill;
import com.company.erp.finance.domain.entity.Invoice;
import com.company.erp.finance.domain.repository.BillRepository;
import com.company.erp.finance.domain.repository.InvoiceRepository;
import com.company.userService.HrModule.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final InvoiceRepository invoiceRepository;
    private final BillRepository billRepository;
    private final PayrollRepository payrollRepository;

    @Transactional(readOnly = true)
    public CashForecastDTO buildForecast(Long companyId, BigDecimal currentCash) {
        LocalDate today = LocalDate.now();
        List<Invoice> invoices = invoiceRepository.findAllInCompany(companyId);
        List<Bill> bills = billRepository.findAllInCompany(companyId);

        BigDecimal inflow = BigDecimal.ZERO;
        BigDecimal outflow = BigDecimal.ZERO;
        BigDecimal inflow30 = BigDecimal.ZERO;
        BigDecimal inflow60 = BigDecimal.ZERO;
        BigDecimal inflow90 = BigDecimal.ZERO;
        BigDecimal outflow30 = BigDecimal.ZERO;
        BigDecimal outflow60 = BigDecimal.ZERO;
        BigDecimal outflow90 = BigDecimal.ZERO;

        for (Invoice invoice : invoices) {
            if (isPaid(invoice.getStatus())) {
                continue;
            }
            BigDecimal amount = safe(invoice.getAmount());
            BigDecimal weighted = amount.multiply(getReceivableWeight(invoice.getDueDate(), today, invoice.getStatus()));
            inflow = inflow.add(weighted);

            LocalDate dueDate = invoice.getDueDate();
            if (dueDate != null) {
                long days = ChronoUnit.DAYS.between(today, dueDate);
                if (days <= 30) {
                    inflow30 = inflow30.add(weighted);
                } else if (days <= 60) {
                    inflow60 = inflow60.add(weighted);
                } else if (days <= 90) {
                    inflow90 = inflow90.add(weighted);
                }
            }
        }

        for (Bill bill : bills) {
            if (isPaid(bill.getStatus())) {
                continue;
            }
            BigDecimal amount = safe(bill.getAmount());
            outflow = outflow.add(amount);

            LocalDate dueDate = bill.getDueDate();
            if (dueDate != null) {
                long days = ChronoUnit.DAYS.between(today, dueDate);
                if (days <= 30) {
                    outflow30 = outflow30.add(amount);
                } else if (days <= 60) {
                    outflow60 = outflow60.add(amount);
                } else if (days <= 90) {
                    outflow90 = outflow90.add(amount);
                }
            }
        }

        BigDecimal payrollOutflow = estimatePayrollOutflow();
        outflow = outflow.add(payrollOutflow);
        outflow30 = outflow30.add(payrollOutflow);

        BigDecimal projection30 = currentCash.add(inflow30).subtract(outflow30);
        BigDecimal projection60 = projection30.add(inflow60).subtract(outflow60);
        BigDecimal projection90 = projection60.add(inflow90).subtract(outflow90);

        return CashForecastDTO.builder()
            .expectedInflow(inflow)
            .expectedOutflow(outflow)
            .projection30(projection30)
            .projection60(projection60)
            .projection90(projection90)
            .cashRunwayDays(BigDecimal.ZERO)
            .build();
    }

    private BigDecimal estimatePayrollOutflow() {
        return payrollRepository.findAll().stream()
            .map(payroll -> safe(payroll.getNetSalary()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isPaid(String status) {
        if (status == null) {
            return false;
        }
        return status.equalsIgnoreCase("PAID") || status.equalsIgnoreCase("CLEARED") || status.equalsIgnoreCase("SETTLED");
    }

    private BigDecimal getReceivableWeight(LocalDate dueDate, LocalDate today, String status) {
        if (status != null && status.equalsIgnoreCase("OVERDUE")) {
            return new BigDecimal("0.60");
        }
        if (dueDate == null) {
            return new BigDecimal("0.50");
        }
        long days = ChronoUnit.DAYS.between(today, dueDate);
        if (days <= 30) {
            return new BigDecimal("0.85");
        }
        if (days <= 60) {
            return new BigDecimal("0.70");
        }
        if (days <= 90) {
            return new BigDecimal("0.55");
        }
        return new BigDecimal("0.30");
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
