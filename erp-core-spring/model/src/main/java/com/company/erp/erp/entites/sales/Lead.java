package com.company.erp.erp.entites.sales;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "LEADS", indexes = {
    @Index(name = "idx_lead_company", columnList = "company_id"),
    @Index(name = "idx_lead_status", columnList = "status"),
    @Index(name = "idx_lead_source", columnList = "source")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LEAD_ID")
    private Long leadId;
    
    @Column(name = "COMPANY_ID", length = 50, nullable = false)
    private String companyId;  // Multi-tenancy
    
    @Column(name = "FIRST_NAME", length = 100, nullable = false)
    private String firstName;
    
    @Column(name = "LAST_NAME", length = 100, nullable = false)
    private String lastName;
    
    @Column(name = "EMAIL", length = 100)
    private String email;
    
    @Column(name = "PHONE", length = 20)
    private String phone;
    
    @Column(name = "COMPANY_NAME", length = 255)
    private String companyName;
    
    @Column(name = "JOB_TITLE", length = 100)
    private String jobTitle;
    
    @Column(name = "SOURCE", length = 50)
    private String source;  // Website, Referral, Trade Show, Cold Call, etc.
    
    @Column(name = "STATUS", length = 50, nullable = false)
    private String status = "NEW";  // NEW, QUALIFIED, IN_PROGRESS, CONVERTED, LOST, DISQUALIFIED
    
    @Column(name = "ESTIMATED_VALUE", precision = 15, scale = 2)
    private BigDecimal estimatedValue;
    
    @Lob
    @Column(name = "NOTES")
    private String notes;
    
    @Column(name = "ASSIGNED_TO", length = 50)
    private String assignedTo;  // User ID
    
    @Column(name = "CONVERSION_DATE")
    private LocalDateTime conversionDate;
    
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
