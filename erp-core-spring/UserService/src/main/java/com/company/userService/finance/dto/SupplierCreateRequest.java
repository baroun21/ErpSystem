package com.company.userService.finance.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierCreateRequest {
    private Long companyId;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String paymentTerms;
    private Boolean isActive;
}
