package com.company.userService.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CreateJournalEntryRequest {
    private String company_id;
    private LocalDate date;
    private String reference;
    private String description;
    private String created_by;
    private String currency;
    private BigDecimal exchange_rate;
    private List<JournalLineRequest> lines;

    public CreateJournalEntryRequest() {
    }

    public CreateJournalEntryRequest(String company_id,
                                     LocalDate date,
                                     String reference,
                                     String description,
                                     String created_by,
                                     String currency,
                                     BigDecimal exchange_rate,
                                     List<JournalLineRequest> lines) {
        this.company_id = company_id;
        this.date = date;
        this.reference = reference;
        this.description = description;
        this.created_by = created_by;
        this.currency = currency;
        this.exchange_rate = exchange_rate;
        this.lines = lines;
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

    public List<JournalLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<JournalLineRequest> lines) {
        this.lines = lines;
    }
}
