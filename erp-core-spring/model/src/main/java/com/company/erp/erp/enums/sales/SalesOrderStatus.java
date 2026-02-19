package com.company.erp.erp.enums.sales;

public enum SalesOrderStatus {
    PENDING("Pending confirmation"),
    CONFIRMED("Order confirmed"),
    SHIPPED("Order shipped"),
    DELIVERED("Order delivered"),
    INVOICED("Order invoiced"),
    CANCELLED("Order cancelled");

    private final String description;

    SalesOrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
