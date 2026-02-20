package com.company.erp.erp.mapper.automation;

import com.company.erp.erp.Dtos.automation.AutomationConditionDTO;
import com.company.erp.erp.entites.automation.AutomationCondition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AutomationConditionMapper {
    AutomationConditionDTO toDTO(AutomationCondition condition);

    AutomationCondition toEntity(AutomationConditionDTO dto);

    void updateEntityFromDTO(AutomationConditionDTO dto, @MappingTarget AutomationCondition condition);
}
