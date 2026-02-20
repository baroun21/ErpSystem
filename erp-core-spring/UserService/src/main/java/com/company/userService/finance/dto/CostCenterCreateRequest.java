package com.company.userService.finance.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenterCreateRequest {
    private Long companyId;
    private String code;
    private String name;
    private String description;
    private Boolean isActive;
}
