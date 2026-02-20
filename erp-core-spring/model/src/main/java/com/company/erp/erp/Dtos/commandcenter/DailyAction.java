package com.company.erp.commandcenter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyAction {
    private String actionType; // APPROVAL_PENDING, OVERDUE_INVOICE, UPCOMING_PAYMENT, LOW_STOCK, PAYMENT_DUE
    private String description;
    private String status; // PENDING, URGENT, COMPLETED
    private String ownerRole; // FINANCE, SALES, OPERATIONS
    private LocalDate dueDate;
    private Long relatedEntityId;
}
