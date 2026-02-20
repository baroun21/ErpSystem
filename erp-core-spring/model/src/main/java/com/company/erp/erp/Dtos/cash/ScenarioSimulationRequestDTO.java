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
public class ScenarioSimulationRequestDTO {
    private Long companyId;
    private BigDecimal inflowMultiplier;
    private BigDecimal outflowMultiplier;
}
