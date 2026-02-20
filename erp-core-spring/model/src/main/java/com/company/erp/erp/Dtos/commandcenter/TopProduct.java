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
public class TopProduct {
    private String productId;
    private String productName;
    private BigDecimal totalQuantitySold;
    private BigDecimal totalRevenue;
}
