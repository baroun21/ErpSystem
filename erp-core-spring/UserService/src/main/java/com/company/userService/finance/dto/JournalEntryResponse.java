package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryResponse {
    private Long id;
    private Long companyId;
    private String entryNumber;
    private LocalDate entryDate;
    private String description;
    private String referenceType;
    private Long referenceId;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
