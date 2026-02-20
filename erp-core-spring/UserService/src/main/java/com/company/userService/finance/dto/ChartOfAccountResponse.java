package com.company.userService.finance.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartOfAccountResponse {
    private Long id;
    private Long companyId;
    private String accountCode;
    private String accountName;
    private String accountType;
    private String accountSubType;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
