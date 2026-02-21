package com.company.erp.procurement.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Request for quotation entity.
 */
@Entity
@Table(name = "rfqs", indexes = {
    @Index(name = "idx_rfq_company", columnList = "company_id"),
    @Index(name = "idx_rfq_number", columnList = "rfq_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rfq extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", foreignKey = @ForeignKey(name = "fk_rfq_vendor"))
    private Vendor vendor;

    @Column(name = "rfq_number", nullable = false, length = 50)
    private String rfqNumber;

    @Column(name = "requested_date")
    private LocalDate requestedDate;

    @Column(name = "status", length = 50)
    private String status; // Draft, Sent, Received, Closed

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}

