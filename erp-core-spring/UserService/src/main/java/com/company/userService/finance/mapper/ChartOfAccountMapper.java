package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.ChartOfAccount;
import com.company.userService.finance.dto.ChartOfAccountCreateRequest;
import com.company.userService.finance.dto.ChartOfAccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChartOfAccountMapper {
    ChartOfAccountMapper INSTANCE = Mappers.getMapper(ChartOfAccountMapper.class);

    ChartOfAccountResponse toResponse(ChartOfAccount entity);
    ChartOfAccount toEntity(ChartOfAccountCreateRequest request);
}
