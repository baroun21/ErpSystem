package com.company.erp.erp.entites.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    private String departmentName;

    private Long managerId; // optional
}

