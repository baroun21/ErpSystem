package com.company.erp.erp.entites.sales;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "SALES_ORDER_LINES", indexes = {
    @Index(name = "idx_sol_company", columnList = "company_id"),
    @Index(name = "idx_sol_order", columnList = "sales_order_id"),
    @Index(name = "idx_sol_product", columnList = "product_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderLine implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long lineId;
    
    @Column(name = "COMPANY_ID", length = 50, nullable = false)
    private String companyId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SALES_ORDER_ID", nullable = false)
    private SalesOrder salesOrder;
    
    @Column(name = "LINE_NUMBER", nullable = false)
    private Integer lineNumber;
    
    @Column(name = "PRODUCT_ID", length = 50, nullable = false)
    private String productId;
    
    @Column(name = "PRODUCT_NAME", length = 255, nullable = false)
    private String productName;
    
    @Column(name = "PRODUCT_SKU", length = 100)
    private String productSku;
    
    @Column(name = "QUANTITY", precision = 10, scale = 4, nullable = false)
    private BigDecimal quantity;
    
    @Column(name = "UNIT_PRICE", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;
    
    @Column(name = "DISCOUNT_PERCENTAGE", precision = 3, scale = 2)
    private BigDecimal discountPercentage;  // 0-100
    
    @Column(name = "DISCOUNT_AMOUNT", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "LINE_TOTAL", precision = 15, scale = 2, nullable = false)
    private BigDecimal lineTotal;
    
    @Column(name = "PROMISED_DELIVERY_DATE")
    private String promisedDeliveryDate;
    
    @Column(name = "DELIVERED_QUANTITY", precision = 10, scale = 4)
    private BigDecimal deliveredQuantity;
    
    @Lob
    @Column(name = "NOTES")
    private String notes;
}
