package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.Supplier;
import com.company.userService.finance.dto.SupplierCreateRequest;
import com.company.userService.finance.dto.SupplierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupplierMapper {
    SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);

    SupplierResponse toResponse(Supplier entity);
    Supplier toEntity(SupplierCreateRequest request);
}
