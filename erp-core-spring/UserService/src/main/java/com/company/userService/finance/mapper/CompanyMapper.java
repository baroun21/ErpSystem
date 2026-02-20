package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.Company;
import com.company.userService.finance.dto.CompanyCreateRequest;
import com.company.userService.finance.dto.CompanyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyResponse toResponse(Company entity);
    Company toEntity(CompanyCreateRequest request);
}
