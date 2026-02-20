package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.BillLine;
import com.company.userService.finance.dto.BillLineCreateRequest;
import com.company.userService.finance.dto.BillLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillLineMapper {
    BillLineMapper INSTANCE = Mappers.getMapper(BillLineMapper.class);

    BillLineResponse toResponse(BillLine entity);
    BillLine toEntity(BillLineCreateRequest request);
}
