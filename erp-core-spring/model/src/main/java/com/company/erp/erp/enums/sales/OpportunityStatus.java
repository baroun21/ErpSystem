package com.company.erp.erp.enums.sales;

public enum OpportunityStatus {
    OPEN("Opportunity is open and being worked"),
    WON("Opportunity won and converted to order"),
    LOST("Opportunity lost to competitor"),
    CLOSED("Opportunity closed without conversion");

    private final String description;

    OpportunityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
