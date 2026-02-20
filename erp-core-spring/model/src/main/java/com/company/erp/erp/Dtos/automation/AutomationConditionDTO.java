package com.company.erp.erp.Dtos.automation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationConditionDTO {
    private Long conditionId;
    private String field;
    private String operator;
    private String value;
}
