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
public class PipelineStageDTO {
    private Long stageId;
    private String companyId;
    private String stageName;
    private Integer stageOrder;
    private BigDecimal conversionProbability;
    private Integer expectedDurationDays;
    private String active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
