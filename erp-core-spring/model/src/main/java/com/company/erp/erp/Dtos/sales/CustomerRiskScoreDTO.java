package com.company.erp.erp.Dtos.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRiskScoreDTO {
    private Long riskScoreId;
    private String companyId;
    private String customerId;
    private String customerName;
    private Integer creditScore;
    private Integer paymentHistoryScore;
    private Integer volumeGrowthScore;
    private Integer latePaymentCount;
    private Integer overdueInvoiceCount;
    private BigDecimal totalOverdueAmount;
    private BigDecimal daysSalesOutstanding;
    private Integer orderFrequencyScore;
    private BigDecimal avgOrderValue;
    private BigDecimal totalLifetimeValue;
    private String riskLevel;
    private BigDecimal overallRiskScore;
    private String riskFactors;
    private String recommendations;
    private BigDecimal creditLimit;
    private BigDecimal currentExposure;
    private String status;
    private LocalDateTime lastReviewDate;
    private LocalDateTime nextReviewDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
