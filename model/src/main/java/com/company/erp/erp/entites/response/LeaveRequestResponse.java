package com.company.erp.erp.entites.response;

import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import com.company.erp.erp.enums.LeaveStatus;
import com.company.erp.erp.enums.LeaveType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestResponse {

    private Long leaveId;

    private EmployeeSummary employee;

    private LeaveType leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private LeaveStatus status;
}

