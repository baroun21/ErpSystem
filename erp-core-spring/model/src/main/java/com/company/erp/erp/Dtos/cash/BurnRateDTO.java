package com.company.erp.erp.Dtos.cash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BurnRateDTO {
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal netCashFlowMonthly;
    private BigDecimal burnRateMonthly;
}
