package com.company.erp.erp.Dtos.cash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashForecastDTO {
    private BigDecimal expectedInflow;
    private BigDecimal expectedOutflow;
    private BigDecimal projection30;
    private BigDecimal projection60;
    private BigDecimal projection90;
    private BigDecimal cashRunwayDays;
}
