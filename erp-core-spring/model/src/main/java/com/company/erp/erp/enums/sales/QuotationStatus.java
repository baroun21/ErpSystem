package com.company.erp.erp.enums.sales;

public enum QuotationStatus {
    DRAFT("Draft quotation"),
    SENT("Quotation sent to customer"),
    ACCEPTED("Quotation accepted by customer"),
    REJECTED("Quotation rejected by customer"),
    CONVERTED_TO_ORDER("Converted to sales order"),
    EXPIRED("Quotation expired");

    private final String description;

    QuotationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
