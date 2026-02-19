package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Cost Center entity
 * Defines cost allocation units for expense tracking and budget management
 */
@Entity
@Table(name = "cost_centers", indexes = {
    @Index(name = "idx_cc_company", columnList = "company_id"),
    @Index(name = "idx_cc_code", columnList = "code")
})
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CostCenter extends BaseEntity {

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "manager", length = 255)
    private String manager;

    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(name = "ytd_actual", precision = 15, scale = 2)
    private BigDecimal ytdActual = BigDecimal.ZERO;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @Column(name = "parent_id")
    private Long parentId; // For hierarchical cost centers

    public CostCenter() {
    }
}
