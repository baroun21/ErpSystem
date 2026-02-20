package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InvoicePostedEvent extends DomainEvent {

    private String invoiceNumber;
    private Long customerId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime postedAt;

    public InvoicePostedEvent(
        Long invoiceId,
        Long companyId,
        String invoiceNumber,
        Long customerId,
        BigDecimal amount,
        String status,
        LocalDateTime postedAt,
        String postedBy
    ) {
        super(invoiceId, "Invoice", companyId, postedBy);
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
        this.postedAt = postedAt;
    }
}
