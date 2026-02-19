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
public class OpportunityDTO {
    private Long opportunityId;
    private String companyId;
    private String opportunityName;
    private Long leadId;
    private Long pipelineStageId;
    private BigDecimal opportunityValue;
    private BigDecimal probabilityPercentage;
    private LocalDateTime expectedCloseDate;
    private LocalDateTime actualCloseDate;
    private String status;
    private String assignedTo;
    private String description;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
