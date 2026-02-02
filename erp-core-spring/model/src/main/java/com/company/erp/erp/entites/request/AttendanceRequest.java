package com.company.erp.erp.entites.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Attendance date is required")
    private LocalDate attendanceDate;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    @DecimalMin(value = "0.0", inclusive = true, message = "Hours worked cannot be negative")
    private Double hoursWorked;
}

