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
public class QuotationDTO {
    private Long quotationId;
    private String companyId;
    private String quoteNumber;
    private Long leadId;
    private LocalDateTime quoteDate;
    private LocalDateTime expiryDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String status;
    private Integer validityDays;
    private String termsAndConditions;
    private String notes;
    private String approvedBy;
    private LocalDateTime approvalDate;
    private Long convertedToSalesOrderId;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
