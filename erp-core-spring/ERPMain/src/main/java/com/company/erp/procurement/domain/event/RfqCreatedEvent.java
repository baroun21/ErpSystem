package com.company.erp.procurement.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Domain event published when an RFQ is created.
 */
@Getter
@Setter
@NoArgsConstructor
public class RfqCreatedEvent extends DomainEvent {

    private String rfqNumber;
    private Long vendorId;
    private LocalDate requestedDate;

    public RfqCreatedEvent(
        Long rfqId,
        Long tenantId,
        String rfqNumber,
        Long vendorId,
        LocalDate requestedDate,
        String triggeredBy
    ) {
        super(rfqId, "Rfq", tenantId, triggeredBy);
        this.rfqNumber = rfqNumber;
        this.vendorId = vendorId;
        this.requestedDate = requestedDate;
    }
}
