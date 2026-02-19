package com.company.erp.hr.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * LeaveRequest entity - tracks employee leave/time-off requests
 * Extends BaseEntity for multi-tenant support (company_id)
 */
@Entity
@Table(name = "leave_requests", indexes = {
    @Index(name = "idx_lr_company", columnList = "company_id"),
    @Index(name = "idx_lr_employee", columnList = "employee_id"),
    @Index(name = "idx_lr_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lr_employee"))
    private Employee employee;

    @Column(name = "leave_type", length = 50)
    private String leaveType;  // Vacation, Sick, Personal, Maternity, etc.

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name = "reason", columnDefinition = "text")
    private String reason;

    @Column(name = "status", length = 50)
    private String status;  // Pending, Approved, Rejected

    // Helper methods
    public long getDurationDays() {
        if (fromDate != null && toDate != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(fromDate, toDate) + 1;
        }
        return 0;
    }

    public boolean isPending() {
        return "Pending".equalsIgnoreCase(status);
    }

    public boolean isApproved() {
        return "Approved".equalsIgnoreCase(status);
    }

    public boolean canBeModified() {
        return isPending();
    }
}
