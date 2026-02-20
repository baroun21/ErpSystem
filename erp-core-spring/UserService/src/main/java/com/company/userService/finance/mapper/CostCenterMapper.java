package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.CostCenter;
import com.company.userService.finance.dto.CostCenterCreateRequest;
import com.company.userService.finance.dto.CostCenterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CostCenterMapper {
    CostCenterMapper INSTANCE = Mappers.getMapper(CostCenterMapper.class);

    CostCenterResponse toResponse(CostCenter entity);
    CostCenter toEntity(CostCenterCreateRequest request);
}
