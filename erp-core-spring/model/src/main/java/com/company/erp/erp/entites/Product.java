package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;
import com.company.erp.erp.enums.CostingMethod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCT", 
       uniqueConstraints = {
           @UniqueConstraint(name = "UK_PROD_SKU", columnNames = "SKU")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long productId;
    
    @Column(name = "SKU", length = 50, nullable = false, unique = true)
    private String sku;
    
    @Column(name = "NAME", length = 255, nullable = false)
    private String name;
    
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;
    
    @Column(name = "CATEGORY", length = 100)
    private String category;
    
    @Column(name = "UNIT_OF_MEASURE", length = 20)
    private String unitOfMeasure;  // EA, BOX, CASE, etc.
    
    @Column(name = "STANDARD_COST", precision = 15, scale = 4)
    private BigDecimal standardCost;
    
    @Column(name = "LIST_PRICE", precision = 15, scale = 4)
    private BigDecimal listPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "COSTING_METHOD", length = 20)
    private CostingMethod costingMethod;  // FIFO, WEIGHTED_AVERAGE, STANDARD
    
    @Column(name = "REORDER_POINT", precision = 10, scale = 2)
    private BigDecimal reorderPoint;
    
    @Column(name = "REORDER_QUANTITY", precision = 10, scale = 2)
    private BigDecimal reorderQuantity;
    
    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductVariant> variants = new HashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<InventoryTransaction> transactions = new HashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ReorderRule> reorderRules = new HashSet<>();
    
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
