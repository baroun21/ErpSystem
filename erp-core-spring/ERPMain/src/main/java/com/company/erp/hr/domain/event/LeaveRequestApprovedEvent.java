package com.company.erp.hr.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Domain event published when a leave request is approved
 * Triggers: Employee notification, Payroll updates, Calendar blocks
 */
@Getter
@Setter
@NoArgsConstructor
public class LeaveRequestApprovedEvent extends DomainEvent {

    private Long employeeId;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String approverName;

    public LeaveRequestApprovedEvent(
        Long leaveRequestId,
        Long tenantId,
        Long employeeId,
        String leaveType,
        LocalDate fromDate,
        LocalDate toDate,
        String approverName,
        String triggeredBy
    ) {
        super(leaveRequestId, "LeaveRequest", tenantId, triggeredBy);
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.approverName = approverName;
    }
}
