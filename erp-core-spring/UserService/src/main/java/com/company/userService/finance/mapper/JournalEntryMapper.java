package com.company.userService.finance.mapper;

import com.company.erp.erp.entites.finance.JournalEntry;
import com.company.userService.finance.dto.JournalEntryCreateRequest;
import com.company.userService.finance.dto.JournalEntryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JournalEntryMapper {
    JournalEntryMapper INSTANCE = Mappers.getMapper(JournalEntryMapper.class);

    JournalEntryResponse toResponse(JournalEntry entity);
    JournalEntry toEntity(JournalEntryCreateRequest request);
}
