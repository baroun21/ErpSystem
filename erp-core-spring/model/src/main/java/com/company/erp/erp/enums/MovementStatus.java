package com.company.erp.erp.enums;

public enum MovementStatus {
    PENDING,      // Movement created, not yet in transit
    IN_TRANSIT,   // Goods are in transit
    RECEIVED,     // Goods received at destination
    REJECTED,     // Goods rejected on receipt
    CANCELLED     // Movement cancelled
}
