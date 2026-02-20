package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.InvoiceLine;
import com.company.userService.finance.dto.InvoiceLineCreateRequest;
import com.company.userService.finance.dto.InvoiceLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceLineMapper {
    InvoiceLineMapper INSTANCE = Mappers.getMapper(InvoiceLineMapper.class);

    InvoiceLineResponse toResponse(InvoiceLine entity);
    InvoiceLine toEntity(InvoiceLineCreateRequest request);
}
