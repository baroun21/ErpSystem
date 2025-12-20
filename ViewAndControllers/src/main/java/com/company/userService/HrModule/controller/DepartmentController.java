package com.company.userService.HrModule.controller;

import com.company.erp.erp.entites.request.DepartmentRequest;
import com.company.erp.erp.entites.response.DepartmentResponse;
import com.company.userService.HrModule.service.DepartmentService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Department Management", description = "Endpoints for managing company departments")
@RestController
@RequestMapping("/api/hr/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    //  Create Department
    @Operation(
            summary = "Create a new department",
            description = "Creates a new department with an optional manager assigned.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Department created successfully",
                            content = @Content(schema = @Schema(implementation = DepartmentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Duplicate department name", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse response = departmentService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //  Get All Departments
    @Operation(
            summary = "Get all departments",
            description = "Fetches a list of all departments in the company."
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        List<DepartmentResponse> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    //  Get Department by ID
    @Operation(
            summary = "Get department by ID",
            description = "Fetches department details by its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Department found",
                            content = @Content(schema = @Schema(implementation = DepartmentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Department not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(
            @Parameter(description = "ID of the department") @PathVariable Long id) {
        DepartmentResponse response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(response);
    }

    //  Update Department
    @Operation(
            summary = "Update a department",
            description = "Updates department information, including the manager assignment.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Department updated successfully",
                            content = @Content(schema = @Schema(implementation = DepartmentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Department not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @Parameter(description = "ID of the department") @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(response);
    }

    //  Delete Department
    @Operation(
            summary = "Delete a department",
            description = "Deletes a department by ID. Use with caution as it may affect employees linked to this department.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Department deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Department not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @Parameter(description = "ID of the department to delete") @PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
