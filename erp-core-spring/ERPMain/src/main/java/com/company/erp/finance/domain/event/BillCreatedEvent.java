package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain event published when a new bill is created.
 */
@Getter
@Setter
@NoArgsConstructor
public class BillCreatedEvent extends DomainEvent {

    private String billNumber;
    private Long supplierId;
    private LocalDate billDate;
    private LocalDate dueDate;
    private BigDecimal amount;

    public BillCreatedEvent(
        Long billId,
        Long tenantId,
        String billNumber,
        Long supplierId,
        LocalDate billDate,
        LocalDate dueDate,
        BigDecimal amount,
        String triggeredBy
    ) {
        super(billId, "Bill", tenantId, triggeredBy);
        this.billNumber = billNumber;
        this.supplierId = supplierId;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.amount = amount;
    }
}
