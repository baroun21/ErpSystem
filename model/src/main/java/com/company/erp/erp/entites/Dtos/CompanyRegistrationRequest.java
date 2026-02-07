package com.company.erp.erp.entites.Dtos;

import lombok.Data;

@Data
public class CompanyRegistrationRequest {
    private String companyName;
    private String domain;
    private String adminUsername;
    private String adminEmail;
    private String adminPassword;
}
