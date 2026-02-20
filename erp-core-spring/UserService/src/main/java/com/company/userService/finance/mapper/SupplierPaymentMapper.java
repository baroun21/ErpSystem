package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.SupplierPayment;
import com.company.userService.finance.dto.SupplierPaymentCreateRequest;
import com.company.userService.finance.dto.SupplierPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupplierPaymentMapper {
    SupplierPaymentMapper INSTANCE = Mappers.getMapper(SupplierPaymentMapper.class);

    SupplierPaymentResponse toResponse(SupplierPayment entity);
    SupplierPayment toEntity(SupplierPaymentCreateRequest request);
}
