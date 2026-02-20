package com.company.erp.erp.mapper.automation;

import com.company.erp.erp.Dtos.automation.AutomationExecutionLogDTO;
import com.company.erp.erp.entites.automation.AutomationExecutionLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutomationExecutionLogMapper {
    @Mapping(target = "ruleId", source = "rule.ruleId")
    @Mapping(target = "ruleName", source = "rule.name")
    AutomationExecutionLogDTO toDTO(AutomationExecutionLog log);
}
