package com.company.erp.hr.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Domain event published when attendance is recorded
 * Triggers: Notification to HR, Payroll updates, Audit logs
 */
@Getter
@Setter
@NoArgsConstructor
public class AttendanceRecordedEvent extends DomainEvent {

    private Long employeeId;
    private LocalDate attendanceDate;
    private String status;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    public AttendanceRecordedEvent(
        Long attendanceId,
        Long tenantId,
        Long employeeId,
        LocalDate attendanceDate,
        String status,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        String triggeredBy
    ) {
        super(attendanceId, "Attendance", tenantId, triggeredBy);
        this.employeeId = employeeId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }
}
