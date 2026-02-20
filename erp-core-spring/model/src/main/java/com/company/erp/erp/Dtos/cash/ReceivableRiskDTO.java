package com.company.erp.erp.Dtos.cash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivableRiskDTO {
    private String topCustomerName;
    private BigDecimal topCustomerShare;
    private BigDecimal overdueRatio;
    private List<MarginTrendPointDTO> marginTrend;
}
