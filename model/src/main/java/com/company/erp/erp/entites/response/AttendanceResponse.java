package com.company.erp.erp.entites.response;

import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceResponse {

    private Long attendanceId;
    private EmployeeSummary employee;
    private LocalDate attendanceDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Double hoursWorked;
}
