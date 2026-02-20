package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartOfAccountCreateRequest {
    private Long companyId;
    private String accountCode;
    private String accountName;
    private String accountType;
    private String accountSubType;
    private String description;
    private Boolean isActive;
}
