package com.company.erp.erp.entites.response;

import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import lombok.Data;

@Data
public class DepartmentResponse {

    private Long departmentId;
    private String departmentName;
    private EmployeeSummary manager;
}

