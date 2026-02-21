package com.company.erp.hr.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Attendance entity - tracks daily attendance records
 * Extends BaseEntity for multi-tenant support (company_id)
 */
@Entity(name = "HrAttendance")
@Table(name = "attendance", indexes = {
    @Index(name = "idx_att_company", columnList = "company_id"),
    @Index(name = "idx_att_employee", columnList = "employee_id"),
    @Index(name = "idx_att_date", columnList = "attendance_date"),
    @Index(name = "idx_att_unique", columnList = "company_id,employee_id,attendance_date", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, foreignKey = @ForeignKey(name = "fk_att_employee"))
    private Employee employee;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "status", length = 50)
    private String status;  // Present, Absent, Late, Half Day

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    // Helper method to get status display
    public boolean isPresent() {
        return "Present".equalsIgnoreCase(status) || "Half Day".equalsIgnoreCase(status);
    }

    public boolean isAbsent() {
        return "Absent".equalsIgnoreCase(status);
    }
}

