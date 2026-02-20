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
public class CashSnapshot {
    private String status; // GREEN, YELLOW, RED based on runway/burn
    private BigDecimal totalCash;
    private BigDecimal reconciledCash;
    private BigDecimal expectedInflow;
    private BigDecimal expectedOutflow;
    private BigDecimal cashRunwayDays;
    private Integer bankAccountCount;
}
