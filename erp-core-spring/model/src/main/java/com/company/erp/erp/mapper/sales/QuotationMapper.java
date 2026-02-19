package com.company.erp.mapper.sales;

import com.company.erp.erp.Dtos.sales.QuotationDTO;
import com.company.erp.erp.entites.sales.Quotation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuotationMapper {
    QuotationDTO toDTO(Quotation quotation);

    Quotation toEntity(QuotationDTO dto);
    
    void updateEntityFromDTO(QuotationDTO dto, @MappingTarget Quotation quotation);
}
