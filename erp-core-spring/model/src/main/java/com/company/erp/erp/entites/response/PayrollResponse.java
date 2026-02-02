package com.company.erp.erp.entites.response;


import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PayrollResponse {

    private Long payrollId;

    private String payPeriod;

    private BigDecimal basicSalary;

    private BigDecimal overtimePay;

    private BigDecimal deductions;

    private BigDecimal netSalary;

    private LocalDate paymentDate;

    // Nested object or simplified employee info
    private EmployeeSummary employee;

}
