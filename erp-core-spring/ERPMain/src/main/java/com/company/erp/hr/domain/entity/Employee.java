package com.company.erp.hr.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Employee entity - represents an employee in the organization
 * Extends BaseEntity for multi-tenant support (company_id), audit trails
 */
@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_emp_company", columnList = "company_id"),
    @Index(name = "idx_emp_department", columnList = "department_id"),
    @Index(name = "idx_emp_user", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends BaseEntity {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", foreignKey = @ForeignKey(name = "fk_employee_position"))
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_employee_department"))
    private Department department;

    @Column(name = "location_id")
    private Long locationId;  // Reference to locations table

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "salary", precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(name = "user_id")
    private Long userId;  // Reference to users table for authentication

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Attendance> attendance;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveRequests;

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
