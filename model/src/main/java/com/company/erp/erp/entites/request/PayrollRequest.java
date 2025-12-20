package com.company.erp.erp.entites.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayrollRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Pay period is required")
    private String payPeriod; // e.g., "2025-09"

    @NotNull(message = "Basic salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Basic salary must be greater than 0")
    private BigDecimal basicSalary;

    @DecimalMin(value = "0.0", inclusive = true, message = "Overtime pay cannot be negative")
    private BigDecimal overtimePay;

    @DecimalMin(value = "0.0", inclusive = true, message = "Deductions cannot be negative")
    private BigDecimal deductions;

    // Normally, net salary can be calculated automatically in the service layer,
    // but if you allow manual input, include this:
    @DecimalMin(value = "0.0", inclusive = false, message = "Net salary must be greater than 0")
    private BigDecimal netSalary;
}
