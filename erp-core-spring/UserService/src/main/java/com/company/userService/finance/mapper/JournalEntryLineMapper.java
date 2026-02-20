package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.JournalEntryLine;
import com.company.userService.finance.dto.JournalEntryLineCreateRequest;
import com.company.userService.finance.dto.JournalEntryLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JournalEntryLineMapper {
    JournalEntryLineMapper INSTANCE = Mappers.getMapper(JournalEntryLineMapper.class);

    JournalEntryLineResponse toResponse(JournalEntryLine entity);
    JournalEntryLine toEntity(JournalEntryLineCreateRequest request);
}
