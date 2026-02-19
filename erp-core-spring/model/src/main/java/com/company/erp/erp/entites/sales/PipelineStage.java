package com.company.erp.erp.entites.sales;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PIPELINE_STAGES", indexes = {
    @Index(name = "idx_stage_company", columnList = "company_id"),
    @Index(name = "idx_stage_order", columnList = "stage_order")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineStage implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STAGE_ID")
    private Long stageId;
    
    @Column(name = "COMPANY_ID", length = 50, nullable = false)
    private String companyId;
    
    @Column(name = "STAGE_NAME", length = 100, nullable = false)
    private String stageName;
    
    @Column(name = "STAGE_ORDER", nullable = false)
    private Integer stageOrder;
    
    @Column(name = "CONVERSION_PROBABILITY", precision = 3, scale = 2)
    private BigDecimal conversionProbability;  // 0.00 to 1.00
    
    @Column(name = "EXPECTED_DURATION_DAYS")
    private Integer expectedDurationDays;
    
    @Column(name = "ACTIVE", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Builder.Default
    private String active = "Y";
    
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
