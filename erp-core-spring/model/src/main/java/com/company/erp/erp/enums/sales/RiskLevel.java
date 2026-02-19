package com.company.erp.erp.enums.sales;

public enum RiskLevel {
    LOW("Low credit risk - score 0-25"),
    MEDIUM("Medium credit risk - score 26-50"),
    HIGH("High credit risk - score 51-75"),
    CRITICAL("Critical credit risk - score 76-100");

    private final String description;

    RiskLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
