package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryCreateRequest {
    private Long companyId;
    private String entryNumber;
    private LocalDate entryDate;
    private String description;
    private String referenceType;
    private Long referenceId;
}
