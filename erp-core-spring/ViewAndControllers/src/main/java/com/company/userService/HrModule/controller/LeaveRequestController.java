package com.company.userService.HrModule.controller;

import com.company.erp.erp.entites.request.LeaveRequestRequest;
import com.company.erp.erp.entites.response.LeaveRequestResponse;
import com.company.erp.erp.enums.LeaveStatus;
import com.company.userService.HrModule.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave Requests", description = "Endpoints for managing employee leave requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    //  Apply for leave
    @PostMapping("/apply")
    @Operation(summary = "Submit a new leave request")
    public ResponseEntity<LeaveRequestResponse> applyLeave(
            @Valid @RequestBody LeaveRequestRequest request) {
        return ResponseEntity.ok(leaveRequestService.createLeaveRequest(request));
    }

    //  Approve leave
    @PutMapping("/{leaveId}/approve")
    @Operation(summary = "Approve a leave request")
    public ResponseEntity<LeaveRequestResponse> approveLeave(
            @Parameter(description = "ID of the leave request to approve") @PathVariable Long leaveId) {
        return ResponseEntity.ok(leaveRequestService.approveLeave(leaveId));
    }

    //  Reject leave
    @PutMapping("/{leaveId}/reject")
    @Operation(summary = "Reject a leave request")
    public ResponseEntity<LeaveRequestResponse> rejectLeave(
            @Parameter(description = "ID of the leave request to reject") @PathVariable Long leaveId) {
        return ResponseEntity.ok(leaveRequestService.rejectLeave(leaveId));
    }

    //  Cancel leave
    @PutMapping("/{leaveId}/cancel")
    @Operation(summary = "Cancel a leave request")
    public ResponseEntity<LeaveRequestResponse> cancelLeave(
            @Parameter(description = "ID of the leave request to cancel") @PathVariable Long leaveId) {
        return ResponseEntity.ok(leaveRequestService.cancelLeave(leaveId));
    }

    //  Get all leave requests
    @GetMapping
    @Operation(summary = "Get all leave requests")
    public ResponseEntity<List<LeaveRequestResponse>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    //  Get leave requests by employee
    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get leave requests by employee ID")
    public ResponseEntity<List<LeaveRequestResponse>> getLeaveRequestsByEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByEmployee(employeeId));
    }

    //  Get leave requests by status (PENDING, APPROVED, REJECTED, CANCELLED)
    @GetMapping("/status/{status}")
    @Operation(summary = "Get leave requests by status")
    public ResponseEntity<List<LeaveRequestResponse>> getLeaveRequestsByStatus(
            @Parameter(description = "Leave status (PENDING, APPROVED, REJECTED, CANCELLED)")
            @PathVariable LeaveStatus status) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByStatus(status));
    }

    //  Optional: Delete leave request (if you want soft delete later)
    @DeleteMapping("/{leaveId}")
    @Operation(summary = "Delete a leave request")
    public ResponseEntity<Void> deleteLeave(
            @Parameter(description = "ID of the leave request to delete") @PathVariable Long leaveId) {
        leaveRequestService.deleteLeave(leaveId);
        return ResponseEntity.noContent().build();
    }
}