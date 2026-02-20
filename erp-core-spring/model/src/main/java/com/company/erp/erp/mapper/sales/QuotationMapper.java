package com.company.erp.mapper.sales;

import com.company.erp.erp.Dtos.sales.QuotationDTO;
import com.company.erp.erp.entites.sales.Quotation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuotationMapper {
    @Mapping(target = "leadId", source = "lead.leadId")
    QuotationDTO toDTO(Quotation quotation);

    @Mapping(target = "lead.leadId", source = "leadId")
    Quotation toEntity(QuotationDTO dto);
    
    @Mapping(target = "lead.leadId", source = "leadId")
    void updateEntityFromDTO(QuotationDTO dto, @MappingTarget Quotation quotation);
}
