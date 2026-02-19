package com.company.erp.erp.entites.sales;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OPPORTUNITIES", indexes = {
    @Index(name = "idx_opp_company", columnList = "company_id"),
    @Index(name = "idx_opp_lead", columnList = "lead_id"),
    @Index(name = "idx_opp_stage", columnList = "pipeline_stage_id"),
    @Index(name = "idx_opp_status", columnList = "status"),
    @Index(name = "idx_opp_close_date", columnList = "expected_close_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Opportunity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OPPORTUNITY_ID")
    private Long opportunityId;
    
    @Column(name = "COMPANY_ID", length = 50, nullable = false)
    private String companyId;
    
    @Column(name = "OPPORTUNITY_NAME", length = 255, nullable = false)
    private String opportunityName;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LEAD_ID", nullable = false)
    private Lead lead;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PIPELINE_STAGE_ID", nullable = false)
    private PipelineStage pipelineStage;
    
    @Column(name = "OPPORTUNITY_VALUE", precision = 15, scale = 2, nullable = false)
    private BigDecimal opportunityValue;
    
    @Column(name = "PROBABILITY_PERCENTAGE", precision = 3, scale = 2)
    private BigDecimal probabilityPercentage;  // 0 to 100
    
    @Column(name = "EXPECTED_CLOSE_DATE")
    private LocalDateTime expectedCloseDate;
    
    @Column(name = "ACTUAL_CLOSE_DATE")
    private LocalDateTime actualCloseDate;
    
    @Column(name = "STATUS", length = 50, nullable = false)
    private String status = "OPEN";  // OPEN, WON, LOST, CLOSED
    
    @Column(name = "ASSIGNED_TO", length = 50)
    private String assignedTo;  // User ID / Sales Rep
    
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Lob
    @Column(name = "NOTES")
    private String notes;
    
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
