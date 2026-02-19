package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * PaymentAppliedEvent
 * Fired when a payment is applied to an invoice or bill
 * Triggers: Invoice/Bill balance reduction, AR/AP aging update
 */
@Getter
@Setter
@NoArgsConstructor
public class PaymentAppliedEvent extends DomainEvent {

    private Long invoiceOrBillId;
    private String documentType; // INVOICE, BILL
    private BigDecimal appliedAmount;
    private BigDecimal remainingBalance;
    private String status; // PARTIAL, FULLY_APPLIED
    private String appliedBy;
    private LocalDateTime appliedDate;

    public PaymentAppliedEvent(
        Long paymentId,
        Long invoiceOrBillId,
        Long companyId,
        String documentType,
        BigDecimal appliedAmount,
        BigDecimal remainingBalance,
        String status,
        String appliedBy,
        LocalDateTime appliedDate
    ) {
        super(paymentId, "Payment", companyId, appliedBy);
        this.invoiceOrBillId = invoiceOrBillId;
        this.documentType = documentType;
        this.appliedAmount = appliedAmount;
        this.remainingBalance = remainingBalance;
        this.status = status;
        this.appliedBy = appliedBy;
        this.appliedDate = appliedDate;
    }
}
