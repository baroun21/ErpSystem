package com.company.erp.commandcenter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpcomingPayment {
    private Long billId;
    private String billNumber;
    private String supplierName;
    private BigDecimal amount;
    private LocalDate dueDate;
    private Integer daysUntilDue;
}
