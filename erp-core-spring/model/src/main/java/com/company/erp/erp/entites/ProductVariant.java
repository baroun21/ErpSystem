package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_VARIANT",
       uniqueConstraints = {
           @UniqueConstraint(name = "UK_VARIANT_SKU", columnNames = "VARIANT_SKU")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VARIANT_ID")
    private Long variantId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;
    
    @Column(name = "VARIANT_SKU", length = 50, nullable = false, unique = true)
    private String variantSku;
    
    @Column(name = "NAME", length = 255, nullable = false)
    private String name;
    
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;
    
    @Column(name = "SIZE", length = 50)
    private String size;
    
    @Column(name = "COLOR", length = 50)
    private String color;
    
    @Column(name = "WEIGHT", precision = 10, scale = 4)
    private BigDecimal weight;
    
    @Column(name = "DIMENSIONS", length = 100)
    private String dimensions;
    
    @Column(name = "COST", precision = 15, scale = 4)
    private BigDecimal cost;
    
    @Column(name = "PRICE", precision = 15, scale = 4)
    private BigDecimal price;
    
    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;
    
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
