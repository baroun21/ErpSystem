package com.company.erp.hr.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Position/Job entity - represents a job position in the organization
 * Extends BaseEntity for multi-tenant support (company_id), audit trails
 */
@Entity
@Table(name = "positions", indexes = {
    @Index(name = "idx_pos_company", columnList = "company_id"),
    @Index(name = "idx_pos_department", columnList = "department_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_position_department"))
    private Department department;

    @Column(name = "level", length = 50)
    private String level;  // Senior, Junior, Manager, etc.

    @Column(name = "salary_range_min", precision = 15, scale = 2)
    private BigDecimal salaryRangeMin;

    @Column(name = "salary_range_max", precision = 15, scale = 2)
    private BigDecimal salaryRangeMax;

    @OneToMany(mappedBy = "position", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Employee> employees;
}

