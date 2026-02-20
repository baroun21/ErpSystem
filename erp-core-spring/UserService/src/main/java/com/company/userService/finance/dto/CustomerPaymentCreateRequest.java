package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPaymentCreateRequest {
    private Long companyId;
    private Long customerId;
    private String paymentNumber;
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private Long invoiceId;
}
