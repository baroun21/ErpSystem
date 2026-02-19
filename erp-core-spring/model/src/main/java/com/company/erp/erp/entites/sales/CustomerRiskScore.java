package com.company.erp.erp.entites.sales;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMER_RISK_SCORES", indexes = {
    @Index(name = "idx_crs_company", columnList = "company_id"),
    @Index(name = "idx_crs_customer", columnList = "customer_id"),
    @Index(name = "idx_crs_risk_level", columnList = "risk_level")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRiskScore implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RISK_SCORE_ID")
    private Long riskScoreId;
    
    @Column(name = "COMPANY_ID", length = 50, nullable = false)
    private String companyId;
    
    @Column(name = "CUSTOMER_ID", length = 50, nullable = false)
    private String customerId;
    
    @Column(name = "CUSTOMER_NAME", length = 255, nullable = false)
    private String customerName;
    
    @Column(name = "CREDIT_SCORE", precision = 3)
    private Integer creditScore;  // 0-100 scale
    
    @Column(name = "PAYMENT_HISTORY_SCORE", precision = 3)
    private Integer paymentHistoryScore;  // Based on on-time payment percentage
    
    @Column(name = "VOLUME_GROWTH_SCORE", precision = 3)
    private Integer volumeGrowthScore;  // Based on order volume trend
    
    @Column(name = "LATE_PAYMENT_COUNT")
    private Integer latePaymentCount;
    
    @Column(name = "OVERDUE_INVOICE_COUNT")
    private Integer overdueInvoiceCount;
    
    @Column(name = "TOTAL_OVERDUE_AMOUNT", precision = 15, scale = 2)
    private BigDecimal totalOverdueAmount;
    
    @Column(name = "DAYS_SALES_OUTSTANDING", precision = 5, scale = 1)
    private BigDecimal daysSalesOutstanding;  // Average DSO
    
    @Column(name = "ORDER_FREQUENCY_SCORE", precision = 3)
    private Integer orderFrequencyScore;  // Based on order frequency
    
    @Column(name = "AVG_ORDER_VALUE", precision = 15, scale = 2)
    private BigDecimal avgOrderValue;
    
    @Column(name = "TOTAL_LIFETIME_VALUE", precision = 15, scale = 2)
    private BigDecimal totalLifetimeValue;
    
    @Column(name = "RISK_LEVEL", length = 20, nullable = false)
    private String riskLevel = "LOW";  // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "OVERALL_RISK_SCORE", precision = 3, scale = 2)
    private BigDecimal overallRiskScore;  // 0 (lowest risk) to 100 (highest risk)
    
    @Lob
    @Column(name = "RISK_FACTORS")
    private String riskFactors;  // JSON or comma-separated list of risk factors
    
    @Lob
    @Column(name = "RECOMMENDATIONS")
    private String recommendations;  // Actions to mitigate risk
    
    @Column(name = "CREDIT_LIMIT", precision = 15, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "CURRENT_EXPOSURE", precision = 15, scale = 2)
    private BigDecimal currentExposure;  // Total outstanding balance
    
    @Column(name = "STATUS", length = 20, nullable = false)
    private String status = "ACTIVE";  // ACTIVE, SUSPENDED, TERMINATED
    
    @Column(name = "LAST_REVIEW_DATE")
    private LocalDateTime lastReviewDate;
    
    @Column(name = "NEXT_REVIEW_DATE")
    private LocalDateTime nextReviewDate;
    
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
