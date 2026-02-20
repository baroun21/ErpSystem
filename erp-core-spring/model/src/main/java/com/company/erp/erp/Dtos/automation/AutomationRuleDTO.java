package com.company.erp.erp.Dtos.automation;

import com.company.erp.erp.entites.automation.ActionType;
import com.company.erp.erp.entites.automation.TriggerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationRuleDTO {
    private Long ruleId;
    private String companyId;
    private String name;
    private String description;
    private TriggerType triggerType;
    private String triggerEvent;
    private String scheduleExpression;
    private String scheduleTimezone;
    private Boolean enabled;
    private ActionType actionType;
    private String actionPayload;
    private LocalDateTime lastTriggeredAt;
    private LocalDateTime nextRunAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AutomationConditionDTO> conditions;
}
