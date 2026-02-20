package com.company.erp.cashintelligence.service;

import com.company.erp.erp.Dtos.cash.MarginTrendPointDTO;
import com.company.erp.erp.Dtos.cash.ReceivableRiskDTO;
import com.company.erp.finance.domain.entity.Bill;
import com.company.erp.finance.domain.entity.Customer;
import com.company.erp.finance.domain.entity.Invoice;
import com.company.erp.finance.domain.repository.BillRepository;
import com.company.erp.finance.domain.repository.CustomerRepository;
import com.company.erp.finance.domain.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceivableRiskService {

    private final InvoiceRepository invoiceRepository;
    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public ReceivableRiskDTO buildReceivableRisk(Long companyId) {
        LocalDate start = LocalDate.now().minusDays(90);
        List<Invoice> invoices = invoiceRepository.findAllInCompany(companyId);
        List<Bill> bills = billRepository.findAllInCompany(companyId);

        Map<Long, BigDecimal> byCustomer = new HashMap<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal overdueTotal = BigDecimal.ZERO;
        LocalDate today = LocalDate.now();

        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceDate() != null && invoice.getInvoiceDate().isBefore(start)) {
                continue;
            }
            BigDecimal amount = safe(invoice.getAmount());
            totalRevenue = totalRevenue.add(amount);
            Long customerId = invoice.getCustomer() != null ? invoice.getCustomer().getId() : null;
            if (customerId != null) {
                byCustomer.put(customerId, byCustomer.getOrDefault(customerId, BigDecimal.ZERO).add(amount));
            }
            if (invoice.getDueDate() != null && invoice.getDueDate().isBefore(today) && !isPaid(invoice.getStatus())) {
                overdueTotal = overdueTotal.add(amount);
            }
        }

        Long topCustomerId = byCustomer.entrySet().stream()
            .max(Comparator.comparing(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse(null);

        String topCustomerName = resolveCustomerName(topCustomerId, companyId);
        BigDecimal topCustomerShare = totalRevenue.signum() == 0
            ? BigDecimal.ZERO
            : byCustomer.getOrDefault(topCustomerId, BigDecimal.ZERO)
                .divide(totalRevenue, 4, java.math.RoundingMode.HALF_UP);

        BigDecimal overdueRatio = totalRevenue.signum() == 0
            ? BigDecimal.ZERO
            : overdueTotal.divide(totalRevenue, 4, java.math.RoundingMode.HALF_UP);

        List<MarginTrendPointDTO> marginTrend = buildMarginTrend(invoices, bills);

        return ReceivableRiskDTO.builder()
            .topCustomerName(topCustomerName)
            .topCustomerShare(topCustomerShare)
            .overdueRatio(overdueRatio)
            .marginTrend(marginTrend)
            .build();
    }

    private List<MarginTrendPointDTO> buildMarginTrend(List<Invoice> invoices, List<Bill> bills) {
        Map<YearMonth, BigDecimal> revenueByMonth = new HashMap<>();
        Map<YearMonth, BigDecimal> expenseByMonth = new HashMap<>();

        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceDate() == null) {
                continue;
            }
            YearMonth month = YearMonth.from(invoice.getInvoiceDate());
            revenueByMonth.put(month, revenueByMonth.getOrDefault(month, BigDecimal.ZERO).add(safe(invoice.getAmount())));
        }

        for (Bill bill : bills) {
            if (bill.getBillDate() == null) {
                continue;
            }
            YearMonth month = YearMonth.from(bill.getBillDate());
            expenseByMonth.put(month, expenseByMonth.getOrDefault(month, BigDecimal.ZERO).add(safe(bill.getAmount())));
        }

        List<MarginTrendPointDTO> trend = new ArrayList<>();
        YearMonth current = YearMonth.now();
        for (int i = 2; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            BigDecimal revenue = revenueByMonth.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal expenses = expenseByMonth.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal marginPercent = revenue.signum() == 0
                ? BigDecimal.ZERO
                : revenue.subtract(expenses)
                    .divide(revenue, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));

            trend.add(MarginTrendPointDTO.builder()
                .period(month.toString())
                .grossMarginPercent(marginPercent)
                .build());
        }
        return trend;
    }

    private String resolveCustomerName(Long customerId, Long companyId) {
        if (customerId == null) {
            return "Unknown";
        }
        return customerRepository.findById(customerId)
            .filter(customer -> customer.getCompanyId().equals(companyId))
            .map(Customer::getName)
            .orElse("Unknown");
    }

    private boolean isPaid(String status) {
        if (status == null) {
            return false;
        }
        return status.equalsIgnoreCase("PAID") || status.equalsIgnoreCase("CLEARED") || status.equalsIgnoreCase("SETTLED");
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
