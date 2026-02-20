package com.company.erp.erp.Dtos.cash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashIntelligenceSummaryDTO {
    private CashPositionDTO cashPosition;
    private CashForecastDTO forecast;
    private BurnRateDTO burnRate;
    private ReceivableRiskDTO receivableRisk;
}
