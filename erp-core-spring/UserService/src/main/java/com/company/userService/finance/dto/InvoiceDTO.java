package com.company.userService.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDTO {
    private String id;
    private String company_id;
    private String customer_id;
    private String customer_name;
    private String invoice_number;
    private LocalDate issue_date;
    private LocalDate due_date;
    private String status;
    private BigDecimal total_amount;
    private BigDecimal tax_amount;
    private BigDecimal paid_amount;
    private BigDecimal balance_due;
    private String currency;
    private String journal_entry_id;
    private String created_by;

    public InvoiceDTO() {
    }

    public InvoiceDTO(String id,
                      String company_id,
                      String customer_id,
                      String customer_name,
                      String invoice_number,
                      LocalDate issue_date,
                      LocalDate due_date,
                      String status,
                      BigDecimal total_amount,
                      BigDecimal tax_amount,
                      BigDecimal paid_amount,
                      BigDecimal balance_due,
                      String currency,
                      String journal_entry_id,
                      String created_by) {
        this.id = id;
        this.company_id = company_id;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.invoice_number = invoice_number;
        this.issue_date = issue_date;
        this.due_date = due_date;
        this.status = status;
        this.total_amount = total_amount;
        this.tax_amount = tax_amount;
        this.paid_amount = paid_amount;
        this.balance_due = balance_due;
        this.currency = currency;
        this.journal_entry_id = journal_entry_id;
        this.created_by = created_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoiceNumber() {
        return invoice_number;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoice_number = invoiceNumber;
    }

    public LocalDate getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(LocalDate issue_date) {
        this.issue_date = issue_date;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public BigDecimal getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(BigDecimal tax_amount) {
        this.tax_amount = tax_amount;
    }

    public BigDecimal getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(BigDecimal paid_amount) {
        this.paid_amount = paid_amount;
    }

    public BigDecimal getBalance_due() {
        return balance_due;
    }

    public void setBalance_due(BigDecimal balance_due) {
        this.balance_due = balance_due;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getJournal_entry_id() {
        return journal_entry_id;
    }

    public void setJournal_entry_id(String journal_entry_id) {
        this.journal_entry_id = journal_entry_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
