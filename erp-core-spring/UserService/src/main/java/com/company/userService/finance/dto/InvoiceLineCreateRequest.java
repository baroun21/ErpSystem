package com.company.userService.finance.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceLineCreateRequest {
    private Long invoiceId;
    private Long costCenterId;
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxAmount;
    private BigDecimal lineTotal;
}
