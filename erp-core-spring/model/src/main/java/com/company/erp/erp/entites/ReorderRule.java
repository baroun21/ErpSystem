package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "REORDER_RULE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReorderRule implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RULE_ID")
    private Long ruleId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "WAREHOUSE_ID", nullable = false)
    private Warehouse warehouse;
    
    @Column(name = "REORDER_LEVEL", precision = 10, scale = 2, nullable = false)
    private BigDecimal reorderLevel;
    
    @Column(name = "REORDER_QUANTITY", precision = 10, scale = 2, nullable = false)
    private BigDecimal reorderQuantity;
    
    @Column(name = "LEAD_TIME_DAYS")
    private Integer leadTimeDays;
    
    @Column(name = "SAFETY_STOCK", precision = 10, scale = 2)
    private BigDecimal safetyStock;
    
    @Column(name = "MAX_STOCK", precision = 10, scale = 2)
    private BigDecimal maxStock;
    
    @Column(name = "ECONOMIC_ORDER_QUANTITY", precision = 10, scale = 2)
    private BigDecimal economicOrderQuantity;
    
    @Column(name = "IS_AUTOMATIC")
    private Boolean isAutomatic = false;
    
    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;
    
    @Column(name = "NOTES", length = 500)
    private String notes;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
