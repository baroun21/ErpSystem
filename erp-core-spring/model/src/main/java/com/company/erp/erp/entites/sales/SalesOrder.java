package com.company.erp.erp.entites.sales;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SALES_ORDERS", indexes = {
    @Index(name = "idx_so_company", columnList = "company_id"),
    @Index(name = "idx_so_customer", columnList = "customer_id"),
    @Index(name = "idx_so_status", columnList = "status"),
    @Index(name = "idx_so_date", columnList = "order_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrder implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SALES_ORDER_ID")
    private Long salesOrderId;
    
    @Column(name = "COMPANY_ID", length = 50, nullable = false)
    private String companyId;
    
    @Column(name = "ORDER_NUMBER", length = 50, nullable = false, unique = true)
    private String orderNumber;
    
    @Column(name = "CUSTOMER_ID", length = 50, nullable = false)
    private String customerId;  // Reference to Customer entity
    
    @Column(name = "CUSTOMER_NAME", length = 255, nullable = false)
    private String customerName;
    
    @Column(name = "ORDER_DATE", nullable = false)
    private LocalDateTime orderDate;
    
    @Column(name = "REQUIRED_DELIVERY_DATE")
    private LocalDateTime requiredDeliveryDate;
    
    @Column(name = "PROMISED_DELIVERY_DATE")
    private LocalDateTime promisedDeliveryDate;
    
    @Column(name = "ACTUAL_DELIVERY_DATE")
    private LocalDateTime actualDeliveryDate;
    
    @Column(name = "SUBTOTAL", precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "TAX_AMOUNT", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(name = "DISCOUNT_AMOUNT", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "TOTAL_AMOUNT", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "STATUS", length = 50, nullable = false)
    private String status = "PENDING";  // PENDING, CONFIRMED, SHIPPED, DELIVERED, INVOICED, CANCELLED
    
    @Column(name = "PAYMENT_STATUS", length = 50, nullable = false)
    private String paymentStatus = "UNPAID";  // UNPAID, PARTIALLY_PAID, PAID, OVERDUE
    
    @Column(name = "SHIPPING_ADDRESS")
    private String shippingAddress;
    
    @Column(name = "BILLING_ADDRESS")
    private String billingAddress;
    
    @Column(name = "REFERENCE_OPPORTUNITY_ID")
    private Long referenceOpportunityId;
    
    @Lob
    @Column(name = "NOTES")
    private String notes;
    
    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SalesOrderLine> orderLines = new ArrayList<>();
    
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
