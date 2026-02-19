package com.company.userService.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class JournalEntryDTO {
    private String id;
    private String company_id;
    private LocalDate date;
    private String reference;
    private String description;
    private String status;
    private String created_by;
    private String currency;
    private BigDecimal exchange_rate;
    private String is_reversal;
    private List<JournalLineDTO> lines;
    private String total_debit;
    private String total_credit;

    public JournalEntryDTO() {
    }

    public JournalEntryDTO(String id,
                           String company_id,
                           LocalDate date,
                           String reference,
                           String description,
                           String status,
                           String created_by,
                           String currency,
                           BigDecimal exchange_rate,
                           String is_reversal,
                           List<JournalLineDTO> lines,
                           String total_debit,
                           String total_credit) {
        this.id = id;
        this.company_id = company_id;
        this.date = date;
        this.reference = reference;
        this.description = description;
        this.status = status;
        this.created_by = created_by;
        this.currency = currency;
        this.exchange_rate = exchange_rate;
        this.is_reversal = is_reversal;
        this.lines = lines;
        this.total_debit = total_debit;
        this.total_credit = total_credit;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(BigDecimal exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getIs_reversal() {
        return is_reversal;
    }

    public void setIs_reversal(String is_reversal) {
        this.is_reversal = is_reversal;
    }

    public List<JournalLineDTO> getLines() {
        return lines;
    }

    public void setLines(List<JournalLineDTO> lines) {
        this.lines = lines;
    }

    public String getTotal_debit() {
        return total_debit;
    }

    public void setTotal_debit(String total_debit) {
        this.total_debit = total_debit;
    }

    public String getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(String total_credit) {
        this.total_credit = total_credit;
    }
}
