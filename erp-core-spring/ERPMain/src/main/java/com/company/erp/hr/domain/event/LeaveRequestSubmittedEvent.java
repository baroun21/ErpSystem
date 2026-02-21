package com.company.erp.hr.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Domain event published when employee submits a leave request
 * Triggers: Manager notification, Audit logs, Calendar updates
 */
@Getter
@Setter
@NoArgsConstructor
public class LeaveRequestSubmittedEvent extends DomainEvent {

    private Long employeeId;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;

    public LeaveRequestSubmittedEvent(
        Long leaveRequestId,
        Long tenantId,
        Long employeeId,
        String leaveType,
        LocalDate fromDate,
        LocalDate toDate,
        String reason,
        String triggeredBy
    ) {
        super(leaveRequestId, "LeaveRequest", tenantId, triggeredBy);
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
    }

    public long getDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(fromDate, toDate) + 1;
    }
}

