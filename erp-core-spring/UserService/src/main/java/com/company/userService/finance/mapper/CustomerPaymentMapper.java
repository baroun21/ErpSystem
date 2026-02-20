package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.CustomerPayment;
import com.company.userService.finance.dto.CustomerPaymentCreateRequest;
import com.company.userService.finance.dto.CustomerPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerPaymentMapper {
    CustomerPaymentMapper INSTANCE = Mappers.getMapper(CustomerPaymentMapper.class);

    CustomerPaymentResponse toResponse(CustomerPayment entity);
    CustomerPayment toEntity(CustomerPaymentCreateRequest request);
}
