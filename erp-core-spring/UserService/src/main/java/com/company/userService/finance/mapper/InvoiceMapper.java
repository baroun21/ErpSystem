package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.Invoice;
import com.company.userService.finance.dto.InvoiceCreateRequest;
import com.company.userService.finance.dto.InvoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceMapper {
    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    InvoiceResponse toResponse(Invoice entity);
    Invoice toEntity(InvoiceCreateRequest request);
}
