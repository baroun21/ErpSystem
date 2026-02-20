package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryLineResponse {
    private Long id;
    private Long journalEntryId;
    private Long accountId;
    private String accountName;
    private String description;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
}
