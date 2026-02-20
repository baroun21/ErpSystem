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
public class SalesSummary {
    private BigDecimal salesToday;
    private Long ordersToday;
    private Integer topProductCount;
    private BigDecimal avgOrderValue;
    private BigDecimal weeklyRevenue;
}
