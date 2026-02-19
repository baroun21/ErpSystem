package com.company.erp.finance.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BankTransactionReceivedEvent
 * Fired when a bank transaction is imported/created
 * Triggers: Bank reconciliation process, available-for-matching notification
 */
@Getter
@Setter
@NoArgsConstructor
public class BankTransactionReceivedEvent extends DomainEvent {

    private Long bankAccountId;
    private String transactionType; // DEBIT, CREDIT, CHEQUE, TRANSFER, FEE, INTEREST
    private BigDecimal amount;
    private String currency;
    private String description;
    private String reference; // Check number, wire reference, etc.
    private String status; // PENDING, CLEARED, REVERSED
    private LocalDateTime transactionDate;
    private String importedBy;
    private LocalDateTime importedDate;

    public BankTransactionReceivedEvent(
        Long bankTransactionId,
        Long bankAccountId,
        Long companyId,
        String transactionType,
        BigDecimal amount,
        String currency,
        String description,
        String reference,
        String status,
        LocalDateTime transactionDate,
        String importedBy,
        LocalDateTime importedDate
    ) {
        super(bankTransactionId, "BankTransaction", companyId, importedBy);
        this.bankAccountId = bankAccountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.reference = reference;
        this.status = status;
        this.transactionDate = transactionDate;
        this.importedBy = importedBy;
        this.importedDate = importedDate;
    }
}
