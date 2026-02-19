package com.company.erp.mapper.sales;

import com.company.erp.erp.Dtos.sales.LeadDTO;
import com.company.erp.erp.entites.sales.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LeadMapper {
    LeadDTO toDTO(Lead lead);

    Lead toEntity(LeadDTO dto);
    
    void updateEntityFromDTO(LeadDTO dto, @MappingTarget Lead lead);
}
