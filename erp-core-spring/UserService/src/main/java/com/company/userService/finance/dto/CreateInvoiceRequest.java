package com.company.userService.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateInvoiceRequest {
    private String company_id;
    private String customer_id;
    private String invoice_number;
    private LocalDate issue_date;
    private LocalDate due_date;
    private BigDecimal total_amount;
    private BigDecimal tax_amount;
    private String currency;
    private String created_by;

    public CreateInvoiceRequest() {
    }

    public CreateInvoiceRequest(String company_id,
                                String customer_id,
                                String invoice_number,
                                LocalDate issue_date,
                                LocalDate due_date,
                                BigDecimal total_amount,
                                BigDecimal tax_amount,
                                String currency,
                                String created_by) {
        this.company_id = company_id;
        this.customer_id = customer_id;
        this.invoice_number = invoice_number;
        this.issue_date = issue_date;
        this.due_date = due_date;
        this.total_amount = total_amount;
        this.tax_amount = tax_amount;
        this.currency = currency;
        this.created_by = created_by;
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

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
