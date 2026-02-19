package com.company.erp.mapper.sales;

import com.company.erp.erp.Dtos.sales.SalesOrderDTO;
import com.company.erp.erp.entites.sales.SalesOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SalesOrderMapper {
    SalesOrderDTO toDTO(SalesOrder salesOrder);

    SalesOrder toEntity(SalesOrderDTO dto);
    
    void updateEntityFromDTO(SalesOrderDTO dto, @MappingTarget SalesOrder salesOrder);
}
