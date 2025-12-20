package com.company.erp.erp.entites.response;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobResponse {
    private Long jobId;
    private String jobTitle;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
}
