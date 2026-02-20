package com.company.erp.commandcenter.service;

import com.company.erp.cashintelligence.service.*;
import com.company.erp.commandcenter.domain.*;
import com.company.erp.finance.domain.entity.*;
import com.company.erp.finance.domain.repository.*;
import com.company.userService.HrModule.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnapshotCacheService {

    private final CashPositionService cashPositionService;
    private final ForecastService forecastService;
    private final BurnRateService burnRateService;
    private final ReceivableRiskService receivableRiskService;

    private final InvoiceRepository invoiceRepository;
    private final BillRepository billRepository;
    private final SalesOrderLineRepository salesOrderLineRepository;
    private final SalesOrderHeaderRepository salesOrderHeaderRepository;

    // In-memory snapshot cache: tenantId (String) -> CommandCenterSnapshot
    private final Map<String, CommandCenterSnapshot> snapshotCache = new ConcurrentHashMap<>();

    /**
     * Retrieve snapshot from cache or build fresh if not cached
     */
    public CommandCenterSnapshot getSnapshot(String companyId) {
        if (snapshotCache.containsKey(companyId)) {
            return snapshotCache.get(companyId);
        }
        return refreshSnapshot(companyId);
    }

    /**
     * Rebuild and cache the complete snapshot for a company
     */
    public CommandCenterSnapshot refreshSnapshot(String companyId) {
        try {
            log.info("Refreshing snapshot for company: {}", companyId);

            // Build each component in parallel where possible
            CashSnapshot cash = buildCashSnapshot(companyId);
            SalesSummary sales = buildSalesSummary(companyId);
            ProfitTrend profit = buildProfitTrend(companyId);
            List<TopProduct> topProducts = buildTopProducts(companyId);
            List<OverdueInvoice> overdueInvoices = buildOverdueInvoices(companyId);
            List<UpcomingPayment> upcomingPayments = buildUpcomingPayments(companyId);
            List<RiskAlert> risks = buildRiskAlerts(companyId, cash);
            List<DailyAction> actions = buildDailyActions(companyId);

            CommandCenterSnapshot snapshot = CommandCenterSnapshot.builder()
                    .companyId(Long.parseLong(companyId))
                    .cashSnapshot(cash)
                    .salesSummary(sales)
                    .profitTrend(profit)
                    .topProducts(topProducts)
                    .overdueInvoices(overdueInvoices)
                    .upcomingPayments(upcomingPayments)
                    .riskAlerts(risks)
                    .dailyActions(actions)
                    .build();

            snapshotCache.put(companyId, snapshot);
            log.info("Snapshot refreshed for company: {}", companyId);
            return snapshot;
        } catch (Exception e) {
            log.error("Failed to refresh snapshot for company: {}", companyId, e);
            // Return empty snapshot to avoid nulls
            return CommandCenterSnapshot.builder()
                    .companyId(Long.parseLong(companyId))
                    .riskAlerts(Collections.emptyList())
                    .dailyActions(Collections.emptyList())
                    .topProducts(Collections.emptyList())
                    .overdueInvoices(Collections.emptyList())
                    .upcomingPayments(Collections.emptyList())
                    .build();
        }
    }

    /**
     * Build cash position snapshot
     */
    private CashSnapshot buildCashSnapshot(String companyId) {
        Long companyIdLong = Long.parseLong(companyId);
        var position = cashPositionService.getCashPosition(companyIdLong);
        var forecast = forecastService.buildForecast(companyIdLong, position.getTotalCash());
        var burnRate = burnRateService.calculateBurnRate(companyIdLong);

        String status = determineCashStatus(forecast.getCashRunwayDays(), burnRate.getBurnRateMonthly());

        return CashSnapshot.builder()
                .status(status)
                .totalCash(position.getTotalCash())
                .reconciledCash(position.getReconciledCash())
                .expectedInflow(forecast.getExpectedInflow())
                .expectedOutflow(forecast.getExpectedOutflow())
                .cashRunwayDays(forecast.getCashRunwayDays())
                .bankAccountCount(position.getAccountCount())
                .build();
    }

    /**
     * Build sales summary for today and this month
     */
    private SalesSummary buildSalesSummary(String companyId) {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);

        // Sales today (sum of SO lines for orders created today, or invoiced today)
        BigDecimal salesToday = salesOrderLineRepository
                .findActiveLinesByCompanyAndDateRange(companyId, today, today.plusDays(1))
                .stream()
                .map(sol -> sol.getLineTotal() != null ? sol.getLineTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sales this month
        BigDecimal salesThisMonth = salesOrderLineRepository
                .findActiveLinesByCompanyAndDateRange(companyId, monthStart, today.plusDays(1))
                .stream()
                .map(sol -> sol.getLineTotal() != null ? sol.getLineTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Orders today
        long ordersToday = salesOrderHeaderRepository
                .findOrdersByCompanyAndDateRange(companyId, today, today.plusDays(1))
                .size();

        return SalesSummary.builder()
                .salesToday(salesToday)
                .ordersToday(ordersToday)
                .topProductCount(6)
                .avgOrderValue(ordersToday > 0 ? salesToday.divide(BigDecimal.valueOf(ordersToday), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .weeklyRevenue(salesThisMonth) // Simplified; can refine to actual week
                .build();
    }

    /**
     * Build profit trend (current vs previous month)
     */
    private ProfitTrend buildProfitTrend(String companyId) {
        LocalDate today = LocalDate.now();
        LocalDate currentMonthStart = today.withDayOfMonth(1);
        LocalDate previousMonthStart = currentMonthStart.minusMonths(1);
        LocalDate previousMonthEnd = currentMonthStart.minusDays(1);

        // Simplified: use GL entries to calculate profit
        // In production, use proper ledger balancing
        BigDecimal currentProfit = calculateProfitForPeriod(companyId, currentMonthStart, today);
        BigDecimal previousProfit = calculateProfitForPeriod(companyId, previousMonthStart, previousMonthEnd);

        BigDecimal changePercent = previousProfit.equals(BigDecimal.ZERO) ? BigDecimal.ZERO :
                currentProfit.subtract(previousProfit)
                        .divide(previousProfit, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

        return ProfitTrend.builder()
                .currentMonthProfit(currentProfit)
                .previousMonthProfit(previousProfit)
                .profitChangePercent(changePercent)
                .grossMarginPercent(new BigDecimal("35.5")) // Placeholder; calculate from GL
                .operatingMarginPercent(new BigDecimal("12.3"))
                .build();
    }

    /**
     * Helper to calculate profit for a period (simplified)
     */
    private BigDecimal calculateProfitForPeriod(String companyId, LocalDate start, LocalDate end) {
        // Simplified: sum revenue - sum expenses from GL
        // In production: use proper trial balance or profit/loss account queries
        return BigDecimal.ZERO; // Placeholder
    }

    /**
     * Build top products by revenue
     */
    private List<TopProduct> buildTopProducts(String companyId) {
        try {
            List<Object[]> results = salesOrderLineRepository.findTopProducts(companyId, org.springframework.data.domain.PageRequest.of(0, 6));
            return results.stream()
                    .map(row -> TopProduct.builder()
                            .productId(String.valueOf(row[0]))
                            .productName((String) row[1])
                            .totalQuantitySold((BigDecimal) row[2])
                            .totalRevenue((BigDecimal) row[3])
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Failed to build top products for company {}: {}", companyId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Build overdue invoices
     */
    private List<OverdueInvoice> buildOverdueInvoices(String companyId) {
        try {
            List<Invoice> overdue = invoiceRepository.findOverdueInCompany(Long.parseLong(companyId), LocalDate.now());
            return overdue.stream()
                    .map(inv -> {
                        long daysPastDue = ChronoUnit.DAYS.between(inv.getDueDate(), LocalDate.now());
                        return OverdueInvoice.builder()
                                .invoiceId(inv.getId())
                                .invoiceNumber(inv.getInvoiceNumber())
                                .customerName(inv.getCustomer() != null ? inv.getCustomer().getName() : "Unknown")
                                .amount(inv.getAmount())
                                .dueDate(inv.getDueDate())
                                .daysPastDue((int) daysPastDue)
                                .build();
                    })
                    .sorted(Comparator.comparingInt(OverdueInvoice::getDaysPastDue).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Failed to build overdue invoices for company {}: {}", companyId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Build upcoming supplier payments (next 30 days)
     */
    private List<UpcomingPayment> buildUpcomingPayments(String companyId) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate futureDate = today.plusDays(30);
            List<Bill> upcoming = billRepository.findUpcomingPayments(Long.parseLong(companyId), today, futureDate);
            return upcoming.stream()
                    .map(bill -> {
                        long daysUntilDue = ChronoUnit.DAYS.between(today, bill.getDueDate());
                        return UpcomingPayment.builder()
                                .billId(bill.getId())
                                .billNumber(bill.getBillNumber())
                                .supplierName(bill.getSupplier() != null ? bill.getSupplier().getName() : "Unknown")
                                .amount(bill.getAmount())
                                .dueDate(bill.getDueDate())
                                .daysUntilDue((int) daysUntilDue)
                                .build();
                    })
                    .sorted(Comparator.comparingInt(UpcomingPayment::getDaysUntilDue))
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Failed to build upcoming payments for company {}: {}", companyId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Build risk alerts based on cash, receivables, and business conditions
     */
    private List<RiskAlert> buildRiskAlerts(String companyId, CashSnapshot cash) {
        List<RiskAlert> alerts = new ArrayList<>();

        // Cash risk: low runway
        if (cash.getCashRunwayDays() != null && cash.getCashRunwayDays().compareTo(new BigDecimal("30")) < 0) {
            alerts.add(RiskAlert.builder()
                    .type("CASH_RISK")
                    .severity("HIGH")
                    .message("Cash runway below 30 days")
                    .exposureAmount(cash.getTotalCash())
                    .build());
        }

        // Receivable risk: high overdue ratio
        try {
            var risk = receivableRiskService.buildReceivableRisk(Long.parseLong(companyId));
            if (risk.getOverdueRatio() != null && risk.getOverdueRatio().compareTo(new BigDecimal("0.15")) > 0) {
                alerts.add(RiskAlert.builder()
                        .type("CREDIT_RISK")
                        .severity("MEDIUM")
                        .message("Overdue receivables exceed 15%")
                        .affectedEntity(risk.getTopCustomerName())
                        .exposureAmount(cash.getTotalCash())
                        .build());
            }
        } catch (Exception e) {
            log.warn("Failed to calculate receivable risk: {}", e.getMessage());
        }

        return alerts;
    }

    /**
     * Build daily actions (approvals, overdue items, etc.)
     */
    private List<DailyAction> buildDailyActions(String companyId) {
        List<DailyAction> actions = new ArrayList<>();

        // Add overdue invoices as daily actions
        List<OverdueInvoice> overdue = buildOverdueInvoices(companyId);
        overdue.stream()
                .limit(3)
                .forEach(inv -> actions.add(DailyAction.builder()
                        .actionType("OVERDUE_INVOICE")
                        .description("Invoice " + inv.getInvoiceNumber() + " overdue by " + inv.getDaysPastDue() + " days")
                        .status("URGENT")
                        .ownerRole("FINANCE")
                        .dueDate(inv.getDueDate())
                        .relatedEntityId(inv.getInvoiceId())
                        .build()));

        // Add upcoming payments as daily actions
        List<UpcomingPayment> upcoming = buildUpcomingPayments(companyId);
        upcoming.stream()
                .filter(p -> p.getDaysUntilDue() <= 7)
                .limit(3)
                .forEach(pmt -> actions.add(DailyAction.builder()
                        .actionType("UPCOMING_PAYMENT")
                        .description("Payment to " + pmt.getSupplierName() + " due in " + pmt.getDaysUntilDue() + " days")
                        .status("PENDING")
                        .ownerRole("FINANCE")
                        .dueDate(pmt.getDueDate())
                        .relatedEntityId(pmt.getBillId())
                        .build()));

        return actions;
    }

    /**
     * Determine cash status based on runway and burn rate
     */
    private String determineCashStatus(BigDecimal runwayDays, BigDecimal burnRate) {
        if (runwayDays == null || runwayDays.compareTo(new BigDecimal("90")) > 0) {
            return "GREEN"; // >90 days runway
        } else if (runwayDays.compareTo(new BigDecimal("30")) > 0) {
            return "YELLOW"; // 30-90 days
        } else {
            return "RED"; // <30 days
        }
    }

    /**
     * Clear cache (useful for testing or forced refresh)
     */
    public void clearCache() {
        snapshotCache.clear();
    }

    /**
     * Clear cache for a specific company
     */
    public void clearCache(String companyId) {
        snapshotCache.remove(companyId);
    }
}
