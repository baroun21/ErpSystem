package com.company.userService.HrModule.controller;

import com.company.erp.erp.entites.request.AttendanceRequest;
import com.company.erp.erp.entites.response.AttendanceResponse;
import com.company.userService.HrModule.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Attendance Management", description = "Manage employee attendance records")
@RestController
@RequestMapping("/api/hr/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    //  Create attendance
    @Operation(
            summary = "Create a new attendance record",
            description = "Creates a new attendance entry for a specific employee on a given date.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Attendance created successfully",
                            content = @Content(schema = @Schema(implementation = AttendanceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<AttendanceResponse> createAttendance(
            @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attendanceService.createAttendance(request));
    }

    //  Get all attendances
    @Operation(
            summary = "Get all attendance records",
            description = "Retrieves a list of all attendance records in the system."
    )
    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    //  Get attendance by ID
    @Operation(
            summary = "Get attendance by ID",
            description = "Fetches the details of a specific attendance record using its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Attendance found",
                            content = @Content(schema = @Schema(implementation = AttendanceResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Attendance not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponse> getAttendanceById(
            @Parameter(description = "ID of the attendance record") @PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    //  Update attendance
    @Operation(
            summary = "Update an existing attendance record",
            description = "Updates the details of an existing attendance record for an employee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Attendance updated successfully",
                            content = @Content(schema = @Schema(implementation = AttendanceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Attendance not found", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponse> updateAttendance(
            @Parameter(description = "ID of the attendance record") @PathVariable Long id,
            @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, request));
    }

    //  Delete attendance (soft delete)
    @Operation(
            summary = "Delete an attendance record",
            description = "Soft deletes an attendance record by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Attendance deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Attendance not found", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(
            @Parameter(description = "ID of the attendance record") @PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
