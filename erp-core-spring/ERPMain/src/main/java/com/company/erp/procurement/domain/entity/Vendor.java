package com.company.erp.procurement.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Procurement vendor entity.
 */
@Entity
@Table(name = "procurement_vendors", indexes = {
    @Index(name = "idx_proc_vendor_company", columnList = "company_id"),
    @Index(name = "idx_proc_vendor_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "contact_name", length = 255)
    private String contactName;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "status", length = 50)
    private String status;
}

