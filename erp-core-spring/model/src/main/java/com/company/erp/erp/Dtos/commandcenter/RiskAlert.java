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
public class RiskAlert {
    private String type; // CREDIT_RISK, CASH_RISK, INVENTORY_RISK, SALES_RISK
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private String message;
    private String affectedEntity; // e.g., customer name, product ID
    private BigDecimal exposureAmount;
}
