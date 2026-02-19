package com.company.erp.finance.domain.entity;

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
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Invoice Line Item
 * Individual line within an invoice
 */
@Entity
@Table(name = "invoice_lines", indexes = {
    @Index(name = "idx_il_company", columnList = "company_id"),
    @Index(name = "idx_il_invoice", columnList = "invoice_id")
})
@Getter
@Setter
@AllArgsConstructor
@Builder
public class InvoiceLine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", foreignKey = @ForeignKey(name = "fk_il_invoice"))
    private Invoice invoice;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "quantity", precision = 12, scale = 4, nullable = false)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "line_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal lineTotal;

    @Column(name = "account_code", length = 50)
    private String accountCode; // Chart of accounts reference

    @Column(name = "cost_center_id")
    private Long costCenterId;

    public InvoiceLine() {
    }

    public void calculateLineTotal() {
        if (quantity != null && unitPrice != null) {
            this.lineTotal = quantity.multiply(unitPrice);
            if (taxAmount != null) {
                this.lineTotal = this.lineTotal.add(taxAmount);
            }
        }
    }
}
