package com.company.erp.erp.Dtos.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadDTO {
    private Long leadId;
    private String companyId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String companyName;
    private String jobTitle;
    private String source;
    private String status;
    private BigDecimal estimatedValue;
    private String notes;
    private String assignedTo;
    private LocalDateTime conversionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
