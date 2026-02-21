package com.company.erp.hr.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * HR Department entity - represents an organizational department
 * Extends BaseEntity for multi-tenant support (company_id), audit trails
 */
@Entity(name = "HrDepartment")
@Table(name = "departments", indexes = {
    @Index(name = "idx_dept_company", columnList = "company_id"),
    @Index(name = "idx_dept_code", columnList = "code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "manager_id")
    private Long managerId;  // Would reference Employee, not creating circular dep

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Position> positions;
}

