package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;
import com.company.erp.erp.enums.MovementType;
import com.company.erp.erp.enums.MovementStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "STOCK_MOVEMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVEMENT_ID")
    private Long movementId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "WAREHOUSE_ID", nullable = false)
    private Warehouse warehouse;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "MOVEMENT_TYPE", length = 20, nullable = false)
    private MovementType movementType;  // RECEIPT, ISSUE, RETURN, INTER_WAREHOUSE_TRANSFER
    
    @Column(name = "MOVEMENT_NUMBER", length = 50, nullable = false)
    private String movementNumber;
    
    @Column(name = "FROM_WAREHOUSE_ID")
    private Long fromWarehouseId;  // For transfers
    
    @Column(name = "REFERENCE_TYPE", length = 50)
    private String referenceType;  // PO, SO, TRANSFER_ORDER, etc.
    
    @Column(name = "REFERENCE_NUMBER", length = 50)
    private String referenceNumber;
    
    @Column(name = "QUANTITY_MOVED", precision = 10, scale = 4, nullable = false)
    private BigDecimal quantityMoved;
    
    @Column(name = "QUANTITY_RECEIVED", precision = 10, scale = 4)
    private BigDecimal quantityReceived;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private MovementStatus status;  // PENDING, IN_TRANSIT, RECEIVED, REJECTED
    
    @Column(name = "NOTES")
    private String notes;
    
    @Column(name = "RECEIVED_DATE")
    private LocalDateTime receivedDate;
    
    @Column(name = "EXPECTED_DATE")
    private LocalDateTime expectedDate;
    
    @Column(name = "RECEIVED_BY", length = 100)
    private String receivedBy;
    
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = MovementStatus.PENDING;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
