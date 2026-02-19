package com.company.erp.hr.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Domain event published when an employee record is updated
 * Triggers: Audit logs, Dependent system updates, Notifications
 */
@Getter
@Setter
@NoArgsConstructor
public class EmployeeUpdatedEvent extends DomainEvent {

    private List<String> changedFields;
    private String oldValue;
    private String newValue;

    public EmployeeUpdatedEvent(
        Long employeeId,
        Long tenantId,
        List<String> changedFields,
        String triggeredBy
    ) {
        super(employeeId, "Employee", tenantId, triggeredBy);
        this.changedFields = changedFields;
    }
}
