package com.company.erp.mapper.sales;

import com.company.erp.erp.Dtos.sales.OpportunityDTO;
import com.company.erp.erp.entites.sales.Opportunity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OpportunityMapper {
    @Mapping(target = "leadId", source = "lead.leadId")
    @Mapping(target = "pipelineStageId", source = "pipelineStage.stageId")
    OpportunityDTO toDTO(Opportunity opportunity);

    @Mapping(target = "lead.leadId", source = "leadId")
    @Mapping(target = "pipelineStage.stageId", source = "pipelineStageId")
    Opportunity toEntity(OpportunityDTO dto);
    
    @Mapping(target = "lead.leadId", source = "leadId")
    @Mapping(target = "pipelineStage.stageId", source = "pipelineStageId")
    void updateEntityFromDTO(OpportunityDTO dto, @MappingTarget Opportunity opportunity);
}
