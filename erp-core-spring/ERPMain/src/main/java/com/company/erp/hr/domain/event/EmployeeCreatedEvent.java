package com.company.erp.hr.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Domain event published when a new employee is hired/created
 * Triggers: Email notifications, User account creation, Audit logs
 */
@Getter
@Setter
@NoArgsConstructor
public class EmployeeCreatedEvent extends DomainEvent {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long departmentId;
    private Long positionId;
    private LocalDate hireDate;

    public EmployeeCreatedEvent(
        Long employeeId,
        Long tenantId,
        String firstName,
        String lastName,
        String email,
        String phone,
        Long departmentId,
        Long positionId,
        LocalDate hireDate,
        String triggeredBy
    ) {
        super(employeeId, "Employee", tenantId, triggeredBy);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.departmentId = departmentId;
        this.positionId = positionId;
        this.hireDate = hireDate;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

