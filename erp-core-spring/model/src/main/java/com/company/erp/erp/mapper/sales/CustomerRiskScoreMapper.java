package com.company.erp.mapper.sales;

import com.company.erp.erp.Dtos.sales.CustomerRiskScoreDTO;
import com.company.erp.erp.entites.sales.CustomerRiskScore;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerRiskScoreMapper {
    CustomerRiskScoreDTO toDTO(CustomerRiskScore customerRiskScore);

    CustomerRiskScore toEntity(CustomerRiskScoreDTO dto);
    
    void updateEntityFromDTO(CustomerRiskScoreDTO dto, @MappingTarget CustomerRiskScore customerRiskScore);
}
