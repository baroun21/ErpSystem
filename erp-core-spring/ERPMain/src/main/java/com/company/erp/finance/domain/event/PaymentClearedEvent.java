package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.*;

import java.time.LocalDateTime;

/**
 * PaymentClearedEvent
 * Fired when a payment is matched/cleared against a bank transaction
 * Triggers: Bank reconciliation completion, invoice/bill marked as paid
 */
@Getter
@Setter
@NoArgsConstructor
public class PaymentClearedEvent extends DomainEvent {

    private Long bankTransactionId;
    private Long bankAccountId;
    private String status; // CLEARED
    private String clearedBy;
    private LocalDateTime clearedDate;
    private String matchReference; // Bank statement reference

    public PaymentClearedEvent(
        Long paymentId,
        Long bankTransactionId,
        Long companyId,
        Long bankAccountId,
        String status,
        String clearedBy,
        LocalDateTime clearedDate,
        String matchReference
    ) {
        super(paymentId, "Payment", companyId, clearedBy);
        this.bankTransactionId = bankTransactionId;
        this.bankAccountId = bankAccountId;
        this.status = status;
        this.clearedBy = clearedBy;
        this.clearedDate = clearedDate;
        this.matchReference = matchReference;
    }
}
