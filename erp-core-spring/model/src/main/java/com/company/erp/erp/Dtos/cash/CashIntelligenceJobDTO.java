package com.company.erp.erp.Dtos.cash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashIntelligenceJobDTO {
    private String jobId;
    private CashIntelligenceJobStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private CashIntelligenceSummaryDTO summary;
    private String error;
}
