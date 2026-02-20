package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountCreateRequest {
    private Long companyId;
    private String accountNumber;
    private String accountName;
    private String bankName;
    private String accountType;
    private BigDecimal balance;
    private Boolean isActive;
}
