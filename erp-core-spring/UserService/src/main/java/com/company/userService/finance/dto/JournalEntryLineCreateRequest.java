package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryLineCreateRequest {
    private Long journalEntryId;
    private Long accountId;
    private String description;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
}
