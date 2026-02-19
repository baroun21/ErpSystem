package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain event published when a new invoice is created.
 */
@Getter
@Setter
@NoArgsConstructor
public class InvoiceCreatedEvent extends DomainEvent {

    private String invoiceNumber;
    private Long customerId;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private BigDecimal amount;

    public InvoiceCreatedEvent(
        Long invoiceId,
        Long tenantId,
        String invoiceNumber,
        Long customerId,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal amount,
        String triggeredBy
    ) {
        super(invoiceId, "Invoice", tenantId, triggeredBy);
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.amount = amount;
    }
}
