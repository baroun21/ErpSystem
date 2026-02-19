package com.company.erp.mapper.sales;

import com.company.erp.erp.Dtos.sales.OpportunityDTO;
import com.company.erp.erp.entites.sales.Opportunity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OpportunityMapper {
    OpportunityDTO toDTO(Opportunity opportunity);

    Opportunity toEntity(OpportunityDTO dto);
    
    void updateEntityFromDTO(OpportunityDTO dto, @MappingTarget Opportunity opportunity);
}
