package com.company.erp.erp.entites.sales;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUOTATIONS", indexes = {
    @Index(name = "idx_quote_company", columnList = "company_id"),
    @Index(name = "idx_quote_lead", columnList = "lead_id"),
    @Index(name = "idx_quote_status", columnList = "status"),
    @Index(name = "idx_quote_expiry", columnList = "expiry_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quotation implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUOTATION_ID")
    private Long quotationId;
    
    @Column(name = "COMPANY_ID", length = 50, nullable = false)
    private String companyId;
    
    @Column(name = "QUOTE_NUMBER", length = 50, nullable = false, unique = true)
    private String quoteNumber;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LEAD_ID", nullable = false)
    private Lead lead;
    
    @Column(name = "QUOTE_DATE", nullable = false)
    private LocalDateTime quoteDate;
    
    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDateTime expiryDate;
    
    @Column(name = "SUBTOTAL", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotal;
    
    @Column(name = "TAX_AMOUNT", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(name = "DISCOUNT_AMOUNT", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "TOTAL_AMOUNT", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "STATUS", length = 50, nullable = false)
    private String status = "DRAFT";  // DRAFT, SENT, ACCEPTED, REJECTED, CONVERTED_TO_ORDER, EXPIRED
    
    @Column(name = "VALIDITY_DAYS")
    private Integer validityDays;  // How many days quote is valid
    
    @Lob
    @Column(name = "TERMS_AND_CONDITIONS")
    private String termsAndConditions;
    
    @Lob
    @Column(name = "NOTES")
    private String notes;
    
    @Column(name = "APPROVED_BY", length = 50)
    private String approvedBy;
    
    @Column(name = "APPROVAL_DATE")
    private LocalDateTime approvalDate;
    
    @Column(name = "CONVERTED_TO_SO_ID")
    private Long convertedToSalesOrderId;
    
    @Column(name = "CREATED_BY", length = 50, nullable = false)
    private String createdBy;
    
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
