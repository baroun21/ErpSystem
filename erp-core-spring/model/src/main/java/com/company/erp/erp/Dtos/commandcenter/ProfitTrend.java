package com.company.erp.commandcenter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfitTrend {
    private BigDecimal currentMonthProfit;
    private BigDecimal previousMonthProfit;
    private BigDecimal profitChangePercent;
    private BigDecimal grossMarginPercent;
    private BigDecimal operatingMarginPercent;
}
