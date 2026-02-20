package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPaymentResponse {
    private Long id;
    private Long companyId;
    private Long customerId;
    private String customerName;
    private String paymentNumber;
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private Long invoiceId;
    private String invoiceNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
