package com.company.userService.HrModule.controller;

import com.company.erp.erp.entites.request.BonusRequest;
import com.company.erp.erp.entites.response.BonusResponse;
import com.company.userService.HrModule.service.BonusService;
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

@Tag(name = "Bonus Management", description = "Endpoints for managing employee bonuses")
@RestController
@RequestMapping("/api/hr/bonuses")
@RequiredArgsConstructor
public class BonusController {

    private final BonusService bonusService;

    @PostMapping("/employee/{employeeId}")
    @Operation(
            summary = "Assign a bonus to an employee",
            description = "Assigns a new bonus to a specific employee by their ID.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Bonus assigned successfully",
                            content = @Content(schema = @Schema(implementation = BonusResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
            }
    )
    public ResponseEntity<BonusResponse> assignBonusToEmployee(
            @Parameter(description = "ID of the employee to assign the bonus to")
            @PathVariable Long employeeId,
            @Valid @RequestBody BonusRequest request) {

        // Set the employeeId from the path variable
        request.setEmployeeId(employeeId);

        BonusResponse response = bonusService.createBonus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //  Get all Bonuses
    @Operation(
            summary = "Get all bonuses",
            description = "Retrieves a list of all bonus records."
    )
    @GetMapping
    public ResponseEntity<List<BonusResponse>> getAllBonuses() {
        return ResponseEntity.ok(bonusService.getAllBonuses());
    }

    //  Get Bonus by ID
    @Operation(
            summary = "Get a bonus by ID",
            description = "Fetches a bonus record using its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bonus found",
                            content = @Content(schema = @Schema(implementation = BonusResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Bonus not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BonusResponse> getBonusById(
            @Parameter(description = "ID of the bonus record") @PathVariable Long id) {
        return ResponseEntity.ok(bonusService.getBonusById(id));
    }

    //  Get Bonuses by Employee ID
    @Operation(
            summary = "Get bonuses by employee ID",
            description = "Retrieves all bonuses granted to a specific employee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bonuses retrieved successfully",
                            content = @Content(schema = @Schema(implementation = BonusResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
            }
    )
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<BonusResponse>> getBonusesByEmployee(
            @Parameter(description = "ID of the employee") @PathVariable Long employeeId) {
        return ResponseEntity.ok(bonusService.getBonusesByEmployeeId(employeeId));
    }

    //  Update Bonus
    @Operation(
            summary = "Update a bonus",
            description = "Updates an existing bonus record.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bonus updated successfully",
                            content = @Content(schema = @Schema(implementation = BonusResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Bonus not found", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BonusResponse> updateBonus(
            @Parameter(description = "ID of the bonus record") @PathVariable Long id,
            @Valid @RequestBody BonusRequest request) {
        return ResponseEntity.ok(bonusService.updateBonus(id, request));
    }

    //  Delete Bonus (soft or hard)
    @Operation(
            summary = "Delete a bonus",
            description = "Deletes a bonus record by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Bonus deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Bonus not found", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBonus(
            @Parameter(description = "ID of the bonus record") @PathVariable Long id) {
        bonusService.deleteBonus(id);
        return ResponseEntity.noContent().build();
    }
}
