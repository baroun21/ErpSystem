package com.company.erp.erp.Dtos.cash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPositionDTO {
    private BigDecimal totalCash;
    private BigDecimal reconciledCash;
    private Integer accountCount;
    private Map<String, BigDecimal> balancesByCurrency;
}
