package com.company.userService.finance.event;

import com.company.userService.finance.dto.InvoiceDTO;
import com.company.userService.finance.dto.JournalEntryDTO;
import java.time.LocalDateTime;

/**
 * Finance domain events for inter-service communication.
 */
public class InvoicePostedEvent {
    private String invoiceId;
    private String invoiceNumber;
    private String customerId;
    private String companyId;
    private InvoiceDTO invoiceData;
    private LocalDateTime occurredAt;

    public InvoicePostedEvent() {
    }

    public InvoicePostedEvent(String invoiceId,
                              String invoiceNumber,
                              String customerId,
                              String companyId,
                              InvoiceDTO invoiceData,
                              LocalDateTime occurredAt) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.companyId = companyId;
        this.invoiceData = invoiceData;
        this.occurredAt = occurredAt;
    }

    public static InvoicePostedEvent from(InvoiceDTO invoice) {
        return new InvoicePostedEvent(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getCustomer_id(),
                invoice.getCompany_id(),
                invoice,
                LocalDateTime.now()
        );
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public InvoiceDTO getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceDTO invoiceData) {
        this.invoiceData = invoiceData;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}

class JournalEntryPostedEvent {
    private String journalEntryId;
    private String reference;
    private String companyId;
    private JournalEntryDTO entryData;
    private LocalDateTime occurredAt;

    public JournalEntryPostedEvent() {
    }

    public JournalEntryPostedEvent(String journalEntryId,
                                   String reference,
                                   String companyId,
                                   JournalEntryDTO entryData,
                                   LocalDateTime occurredAt) {
        this.journalEntryId = journalEntryId;
        this.reference = reference;
        this.companyId = companyId;
        this.entryData = entryData;
        this.occurredAt = occurredAt;
    }

    public static JournalEntryPostedEvent from(JournalEntryDTO entry) {
        return new JournalEntryPostedEvent(
                entry.getId(),
                entry.getReference(),
                entry.getCompany_id(),
                entry,
                LocalDateTime.now()
        );
    }

    public String getJournalEntryId() {
        return journalEntryId;
    }

    public void setJournalEntryId(String journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public JournalEntryDTO getEntryData() {
        return entryData;
    }

    public void setEntryData(JournalEntryDTO entryData) {
        this.entryData = entryData;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}

class PaymentReceivedEvent {
    private String paymentId;
    private String customerId;
    private String companyId;
    private String invoiceId;
    private String amount;
    private LocalDateTime occurredAt;

    public PaymentReceivedEvent() {
    }

    public PaymentReceivedEvent(String paymentId,
                                String customerId,
                                String companyId,
                                String invoiceId,
                                String amount,
                                LocalDateTime occurredAt) {
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.companyId = companyId;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.occurredAt = occurredAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}

class BillPostedEvent {
    private String billId;
    private String billNumber;
    private String supplierId;
    private String companyId;
    private LocalDateTime occurredAt;

    public BillPostedEvent() {
    }

    public BillPostedEvent(String billId,
                           String billNumber,
                           String supplierId,
                           String companyId,
                           LocalDateTime occurredAt) {
        this.billId = billId;
        this.billNumber = billNumber;
        this.supplierId = supplierId;
        this.companyId = companyId;
        this.occurredAt = occurredAt;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
