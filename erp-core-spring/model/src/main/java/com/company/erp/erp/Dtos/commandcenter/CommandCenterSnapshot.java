package com.company.erp.commandcenter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandCenterSnapshot {
    private Long companyId;
    private CashSnapshot cashSnapshot;
    private SalesSummary salesSummary;
    private ProfitTrend profitTrend;
    private List<RiskAlert> riskAlerts;
    private List<DailyAction> dailyActions;
    private List<TopProduct> topProducts;
    private List<OverdueInvoice> overdueInvoices;
    private List<UpcomingPayment> upcomingPayments;
}
