package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMPANY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "COMPANY_ID")
    private Long companyId;

    @Column(name = "COMPANY_NAME", nullable = false, length = 150)
    private String companyName;

    @Column(name = "DOMAIN", unique = true)
    private String domain;

    @Column(name = "STATUS", length = 20)
    private String status = "ACTIVE";

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "ACTIVE";
    }
}