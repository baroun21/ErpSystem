package com.company.userService.HrModule.service;

import com.company.erp.erp.entites.Attendance;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import com.company.erp.erp.entites.request.AttendanceRequest;
import com.company.erp.erp.entites.response.AttendanceResponse;

import com.company.erp.erp.mapper.AttendanceMapper;
import com.company.userService.HrModule.exceptions.ResourceNotFoundException;
import com.company.userService.HrModule.repository.AttendanceRepository;
import com.company.userService.HrModule.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    /**
     * Create a new attendance record
     */
    public AttendanceResponse createAttendance(AttendanceRequest request) {
        // Validate and fetch employee
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + request.getEmployeeId()));

        // Convert request to entity
        Attendance attendance = attendanceMapper.toEntity(request);
        attendance.setEmployee(employee);

        // Auto-calculate hours if both check-in and check-out provided
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            double hours = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime()).toMinutes() / 60.0;
            attendance.setHoursWorked(hours);
        }

        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponse(saved);
    }

    /**
     * Get all attendance records
     */
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendances() {
        return attendanceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get attendance by ID
     */
    @Transactional(readOnly = true)
    public AttendanceResponse getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));
        return mapToResponse(attendance);
    }

    /**
     * Update existing attendance record
     */
    public AttendanceResponse updateAttendance(Long id, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));

        // Fetch employee (if ID provided)
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + request.getEmployeeId()));

        // Update entity fields
        attendanceMapper.updateEntityFromRequest(request, attendance);
        attendance.setEmployee(employee);

        // Recalculate hours if applicable
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            double hours = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime()).toMinutes() / 60.0;
            attendance.setHoursWorked(hours);
        }

        Attendance updated = attendanceRepository.save(attendance);
        return mapToResponse(updated);
    }

    /**
     * Delete attendance record
     */
    public void deleteAttendance(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));
        attendanceRepository.delete(attendance);
    }

    /**
     * Helper method: Map Attendance entity → AttendanceResponse
     * and include EmployeeSummary
     */
    private AttendanceResponse mapToResponse(Attendance attendance) {
        AttendanceResponse response = attendanceMapper.toResponse(attendance);

        if (attendance.getEmployee() != null) {
            Employee emp = attendance.getEmployee();
            EmployeeSummary summary = EmployeeSummary.builder()
                    .employeeId(emp.getEmployeeId())
                    .firstName(emp.getFirstName())
                    .lastName(emp.getLastName())
                    .jobTitle(emp.getJob() != null ? emp.getJob().getJobTitle() : null)
                    .build();
            response.setEmployee(summary);
        }

        return response;
    }
}
