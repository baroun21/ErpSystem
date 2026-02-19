package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * PaymentReceivedEvent
 * Fired when a payment is created/received from customer or supplier
 * Triggers: AR/AP reduction, bank account update, invoice/bill partial payment
 */
@Getter
@Setter
@NoArgsConstructor
public class PaymentReceivedEvent extends DomainEvent {

    private String paymentNumber;
    private String paymentType; // CUSTOMER_PAYMENT, SUPPLIER_PAYMENT, OTHER
    private Long relatedEntityId; // invoice_id or bill_id
    private Long bankAccountId;
    private BigDecimal amount;
    private String currency;
    private String status; // DRAFT, RECEIVED, CLEARED
    private String receivedBy;
    private LocalDateTime receivedDate;

    public PaymentReceivedEvent(
        Long paymentId,
        Long companyId,
        String paymentNumber,
        String paymentType,
        Long relatedEntityId,
        Long bankAccountId,
        BigDecimal amount,
        String currency,
        String status,
        String receivedBy,
        LocalDateTime receivedDate
    ) {
        super(paymentId, "Payment", companyId, receivedBy);
        this.paymentNumber = paymentNumber;
        this.paymentType = paymentType;
        this.relatedEntityId = relatedEntityId;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.receivedBy = receivedBy;
        this.receivedDate = receivedDate;
    }
}
