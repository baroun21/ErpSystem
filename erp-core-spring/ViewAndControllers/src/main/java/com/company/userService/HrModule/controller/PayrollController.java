package com.company.userService.HrModule.controller;

import com.company.erp.erp.entites.request.PayrollRequest;
import com.company.erp.erp.entites.response.PayrollResponse;
import com.company.userService.HrModule.service.PayrollService;
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

@Tag(name = "Payroll Management", description = "Endpoints for managing employee payrolls in the HR module")
@RestController
@RequestMapping("/api/hr/payrolls")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    // CREATE PAYROLL
    @Operation(
            summary = "Create a new payroll entry",
            description = "Adds a payroll record for an employee, including salary, overtime, deductions, and net pay.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Payroll created successfully",
                            content = @Content(schema = @Schema(implementation = PayrollResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid payroll data", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<PayrollResponse> createPayroll(
            @Valid @RequestBody PayrollRequest request
    ) {
        PayrollResponse response = payrollService.createPayroll(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET ALL PAYROLLS
    @Operation(
            summary = "Retrieve all payrolls",
            description = "Fetches a list of all payroll records for all employees.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payroll records retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PayrollResponse.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<PayrollResponse>> getAllPayrolls() {
        List<PayrollResponse> payrolls = payrollService.getAllPayrolls();
        return ResponseEntity.ok(payrolls);
    }

    // GET PAYROLL BY ID
    @Operation(
            summary = "Get payroll by ID",
            description = "Retrieves detailed payroll information for a specific payroll entry using its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payroll record found",
                            content = @Content(schema = @Schema(implementation = PayrollResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Payroll record not found", content = @Content)
            }
    )

    @GetMapping("/{payrollId}")
    public ResponseEntity<PayrollResponse> getPayrollById(
            @Parameter(description = "ID of the payroll entry to retrieve") @PathVariable Long payrollId
    ) {
        PayrollResponse response = payrollService.getPayrollById(payrollId);
        return ResponseEntity.ok(response);
    }

    // UPDATE PAYROLL
    @Operation(
            summary = "Update payroll information",
            description = "Updates an existing payroll entry, including salary details and net pay.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payroll updated successfully",
                            content = @Content(schema = @Schema(implementation = PayrollResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid payroll data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Payroll record not found", content = @Content)
            }
    )
    @PutMapping("/{payrollId}")
    public ResponseEntity<PayrollResponse> updatePayroll(
            @Parameter(description = "ID of the payroll entry to update") @PathVariable Long payrollId,
            @Valid @RequestBody PayrollRequest request
    ) {
        PayrollResponse response = payrollService.updatePayroll(payrollId, request);
        return ResponseEntity.ok(response);
    }

    // DELETE PAYROLL
    @Operation(
            summary = "Delete a payroll entry",
            description = "Removes a payroll record from the system.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Payroll deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Payroll record not found", content = @Content)
            }
    )
    @DeleteMapping("/{payrollId}")
    public ResponseEntity<Void> deletePayroll(
            @Parameter(description = "ID of the payroll entry to delete") @PathVariable Long payrollId
    ) {
        payrollService.deletePayroll(payrollId);
        return ResponseEntity.noContent().build();
    }
}
