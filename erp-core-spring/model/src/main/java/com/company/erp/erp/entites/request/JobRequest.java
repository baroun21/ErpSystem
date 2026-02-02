package com.company.erp.erp.entites.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class JobRequest {

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum salary cannot be negative")
    private BigDecimal minSalary;

    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum salary cannot be negative")
    private BigDecimal maxSalary;
}

