package com.company.erp.erp.Dtos.automation;

import com.company.erp.erp.entites.automation.ActionType;
import com.company.erp.erp.entites.automation.ExecutionStatus;
import com.company.erp.erp.entites.automation.TriggerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationExecutionLogDTO {
    private Long logId;
    private Long ruleId;
    private String ruleName;
    private TriggerType triggerType;
    private String triggerSource;
    private ActionType actionType;
    private ExecutionStatus status;
    private String message;
    private String errorDetails;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime createdAt;
}
