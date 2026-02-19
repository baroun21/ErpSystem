package com.company.erp.erp.enums;

public enum MovementType {
    RECEIPT,                  // Incoming stock from PO
    ISSUE,                    // Outgoing stock from SO
    RETURN,                   // Return from customer
    ADJUSTMENT,               // Inventory adjustment/cycle count
    INTER_WAREHOUSE_TRANSFER  // Transfer between warehouses
}
