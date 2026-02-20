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
public class BillPostedEvent extends DomainEvent {

    private String billNumber;
    private Long supplierId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime postedAt;

    public BillPostedEvent(
        Long billId,
        Long companyId,
        String billNumber,
        Long supplierId,
        BigDecimal amount,
        String status,
        LocalDateTime postedAt,
        String postedBy
    ) {
        super(billId, "Bill", companyId, postedBy);
        this.billNumber = billNumber;
        this.supplierId = supplierId;
        this.amount = amount;
        this.status = status;
        this.postedAt = postedAt;
    }
}
