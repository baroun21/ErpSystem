package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.BankAccount;
import com.company.userService.finance.dto.BankAccountCreateRequest;
import com.company.userService.finance.dto.BankAccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountMapper {
    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    BankAccountResponse toResponse(BankAccount entity);
    BankAccount toEntity(BankAccountCreateRequest request);
}
