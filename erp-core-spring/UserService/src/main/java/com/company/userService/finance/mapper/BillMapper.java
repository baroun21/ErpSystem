package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.Bill;
import com.company.userService.finance.dto.BillCreateRequest;
import com.company.userService.finance.dto.BillResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    BillResponse toResponse(Bill entity);
    Bill toEntity(BillCreateRequest request);
}
