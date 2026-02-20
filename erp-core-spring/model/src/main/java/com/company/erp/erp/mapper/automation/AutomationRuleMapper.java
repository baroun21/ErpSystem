package com.company.erp.erp.mapper.automation;

import com.company.erp.erp.Dtos.automation.AutomationRuleDTO;
import com.company.erp.erp.entites.automation.AutomationRule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = AutomationConditionMapper.class)
public interface AutomationRuleMapper {
    AutomationRuleDTO toDTO(AutomationRule rule);

    AutomationRule toEntity(AutomationRuleDTO dto);

    void updateEntityFromDTO(AutomationRuleDTO dto, @MappingTarget AutomationRule rule);
}
