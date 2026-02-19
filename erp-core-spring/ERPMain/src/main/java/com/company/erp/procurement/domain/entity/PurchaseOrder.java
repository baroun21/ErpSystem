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

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Purchase order entity.
 */
@Entity
@Table(name = "purchase_orders", indexes = {
    @Index(name = "idx_po_company", columnList = "company_id"),
    @Index(name = "idx_po_number", columnList = "po_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", foreignKey = @ForeignKey(name = "fk_po_vendor"))
    private Vendor vendor;

    @Column(name = "po_number", nullable = false, length = 50)
    private String poNumber;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "status", length = 50)
    private String status; // Draft, Submitted, Approved, Ordered, Received

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}
