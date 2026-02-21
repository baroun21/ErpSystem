package com.company.erp.procurement.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain event published when a purchase order is created.
 */
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderCreatedEvent extends DomainEvent {

    private String poNumber;
    private Long vendorId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;

    public PurchaseOrderCreatedEvent(
        Long purchaseOrderId,
        Long tenantId,
        String poNumber,
        Long vendorId,
        LocalDate orderDate,
        BigDecimal totalAmount,
        String triggeredBy
    ) {
        super(purchaseOrderId, "PurchaseOrder", tenantId, triggeredBy);
        this.poNumber = poNumber;
        this.vendorId = vendorId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }
}

