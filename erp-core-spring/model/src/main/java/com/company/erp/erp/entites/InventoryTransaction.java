package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;
import com.company.erp.erp.enums.MovementType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "INVENTORY_TRANSACTION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "WAREHOUSE_ID", nullable = false)
    private Warehouse warehouse;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "MOVEMENT_TYPE", length = 20, nullable = false)
    private MovementType movementType;  // RECEIPT, ISSUE, RETURN, ADJUSTMENT
    
    @Column(name = "TRANSACTION_NUMBER", length = 50)
    private String transactionNumber;
    
    @Column(name = "REFERENCE_TYPE", length = 50)
    private String referenceType;  // PO, SO, TRANSFER, JOURNAL_ENTRY
    
    @Column(name = "REFERENCE_NUMBER", length = 50)
    private String referenceNumber;
    
    @Column(name = "QUANTITY", precision = 10, scale = 4, nullable = false)
    private BigDecimal quantity;
    
    @Column(name = "UNIT_COST", precision = 15, scale = 4)
    private BigDecimal unitCost;
    
    @Column(name = "TOTAL_COST", precision = 15, scale = 4)
    private BigDecimal totalCost;
    
    @Column(name = "NOTES", length = 500)
    private String notes;
    
    @Column(name = "TRANSACTION_DATE", nullable = false)
    private LocalDateTime transactionDate;
    
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (transactionDate == null) {
            transactionDate = LocalDateTime.now();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
