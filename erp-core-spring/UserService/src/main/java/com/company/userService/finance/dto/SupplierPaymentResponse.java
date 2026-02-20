package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierPaymentResponse {
    private Long id;
    private Long companyId;
    private Long supplierId;
    private String supplierName;
    private String paymentNumber;
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private Long billId;
    private String billNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
