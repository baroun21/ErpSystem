package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WAREHOUSE",
       uniqueConstraints = {
           @UniqueConstraint(name = "UK_WAREHOUSE_CODE", columnNames = "WAREHOUSE_CODE")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WAREHOUSE_ID")
    private Long warehouseId;
    
    @Column(name = "WAREHOUSE_CODE", length = 20, nullable = false, unique = true)
    private String warehouseCode;
    
    @Column(name = "NAME", length = 255, nullable = false)
    private String name;
    
    @Column(name = "LOCATION", length = 255)
    private String location;
    
    @Column(name = "ADDRESS", length = 500)
    private String address;
    
    @Column(name = "CITY", length = 100)
    private String city;
    
    @Column(name = "STATE_PROVINCE", length = 100)
    private String stateProvince;
    
    @Column(name = "POSTAL_CODE", length = 20)
    private String postalCode;
    
    @Column(name = "COUNTRY", length = 100)
    private String country;
    
    @Column(name = "MANAGER_NAME", length = 150)
    private String managerName;
    
    @Column(name = "MANAGER_EMAIL", length = 100)
    private String managerEmail;
    
    @Column(name = "MANAGER_PHONE", length = 30)
    private String managerPhone;
    
    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;
    
    @Column(name = "IS_RECEIVING")
    private Boolean isReceiving = true;
    
    @Column(name = "IS_SHIPPING")
    private Boolean isShipping = true;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StockMovement> stockMovements = new HashSet<>();
    
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<InventoryTransaction> transactions = new HashSet<>();
    
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
