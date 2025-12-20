package com.company.userService.HrModule.service;

import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.LeaveRequest;
import com.company.erp.erp.entites.request.LeaveRequestRequest;
import com.company.erp.erp.entites.response.LeaveRequestResponse;
import com.company.erp.erp.enums.LeaveStatus;
import com.company.erp.erp.mapper.LeaveRequestMapper;

import com.company.userService.HrModule.exceptions.DuplicateRecordException;
import com.company.userService.HrModule.exceptions.ResourceNotFoundException;
import com.company.userService.HrModule.repository.EmployeeRepository;
import com.company.userService.HrModule.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing employee leave requests.
 * Handles validation, overlap checks, and status updates.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestMapper leaveRequestMapper;


     // Create a new leave request for an employee.

    public LeaveRequestResponse createLeaveRequest(LeaveRequestRequest request) {
        // 1️⃣ Get the employee
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + request.getEmployeeId()));

        // 2️⃣ Validate leave date range
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        // 3️⃣ Check for overlapping requests
        boolean overlapExists = leaveRequestRepository.existsByEmployeeAndDateRange(
                request.getEmployeeId(), request.getStartDate(), request.getEndDate()
        );
        if (overlapExists) {
            throw new DuplicateRecordException("Overlapping leave request already exists for this employee.");
        }

        // 4️⃣ Map and save the entity
        LeaveRequest entity = leaveRequestMapper.toEntity(request, employee);
        entity.setStatus(LeaveStatus.PENDING);
        LeaveRequest saved = leaveRequestRepository.save(entity);

        return leaveRequestMapper.toResponse(saved);
    }

    /**
     * Retrieve all leave requests.
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getAllLeaveRequests() {
        return leaveRequestRepository.findAll()
                .stream()
                .map(leaveRequestMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all leave requests for a specific employee.
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getLeaveRequestsByEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(leaveRequestMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve leave requests by status (PENDING, APPROVED, REJECTED, CANCELLED).
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> getLeaveRequestsByStatus(LeaveStatus status) {
        return leaveRequestRepository.findByStatus(status)
                .stream()
                .map(leaveRequestMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Approve a leave request.
     */
    public LeaveRequestResponse approveLeave(Long leaveId) {
        LeaveRequest leave = getLeaveByIdOrThrow(leaveId);
        leave.setStatus(LeaveStatus.APPROVED);
        return leaveRequestMapper.toResponse(leaveRequestRepository.save(leave));
    }

    /**
     * Reject a leave request.
     */
    public LeaveRequestResponse rejectLeave(Long leaveId) {
        LeaveRequest leave = getLeaveByIdOrThrow(leaveId);
        leave.setStatus(LeaveStatus.REJECTED);
        return leaveRequestMapper.toResponse(leaveRequestRepository.save(leave));
    }

    /**
     * Cancel a leave request.
     */
    public LeaveRequestResponse cancelLeave(Long leaveId) {
        LeaveRequest leave = getLeaveByIdOrThrow(leaveId);
        leave.setStatus(LeaveStatus.CANCELLED);
        return leaveRequestMapper.toResponse(leaveRequestRepository.save(leave));
    }

    /**
     * Soft delete / deactivate a leave request if needed later.
     */
    public void deleteLeave(Long leaveId) {
        LeaveRequest leave = getLeaveByIdOrThrow(leaveId);
        leaveRequestRepository.delete(leave);
    }

    /**
     * Helper method to safely find a leave request by ID.
     */
    private LeaveRequest getLeaveByIdOrThrow(Long leaveId) {
        return leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + leaveId));
    }
}
