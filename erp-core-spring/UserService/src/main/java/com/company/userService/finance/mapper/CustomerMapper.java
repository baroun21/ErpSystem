package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.Customer;
import com.company.userService.finance.dto.CustomerCreateRequest;
import com.company.userService.finance.dto.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerResponse toResponse(Customer entity);
    Customer toEntity(CustomerCreateRequest request);
}
