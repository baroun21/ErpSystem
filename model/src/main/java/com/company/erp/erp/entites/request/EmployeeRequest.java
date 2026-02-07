package com.company.erp.erp.entites.request;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Data
public class EmployeeRequest {

    private Long employeeId;
    private Long managerId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    private String phoneNumber;

    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private Long departmentId;

    @DecimalMin(value = "0.0", inclusive = true, message = "Salary must be non-negative")
    private BigDecimal salary;

//    private Employee managerId;

    private Long userId;
//
//    public void setEmployeeId(Long employeeId) {
//        this.employeeId = employeeId;
//    }
//
////    public Long getManagerId() {
////        return managerId;
////    }
////
////    public void setManagerId(Long managerId) {
////        this.managerId = managerId;
////    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public void setHireDate(LocalDate hireDate) {
//        this.hireDate = hireDate;
//    }
//
//    public void setJobId(Long jobId) {
//        this.jobId = jobId;
//    }
//
//    public void setDepartmentId(Long departmentId) {
//        this.departmentId = departmentId;
//    }
//
//    public void setSalary(BigDecimal salary) {
//        this.salary = salary;
//    }
//
////    public void setManager(Employee manager) {
////        this.manager = manager;
////    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
}
