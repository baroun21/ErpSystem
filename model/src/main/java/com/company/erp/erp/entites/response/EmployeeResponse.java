package com.company.erp.erp.entites.response;


import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeResponse {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    private BigDecimal salary;

    private JobResponse job;
    private DepartmentResponse department;
    private EmployeeSummary managerId;
    //    private UserResponse user;
    private EmployeeSummary employeeSummary;
}

