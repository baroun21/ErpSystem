package com.company.userService.HrModule.controller;

import com.company.erp.erp.entites.request.JobRequest;
import com.company.erp.erp.entites.response.JobResponse;
import com.company.userService.HrModule.service.JobService;
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

@Tag(name = "Job Management", description = "Endpoints for managing job positions in the HR module")
@RestController
@RequestMapping("/api/hr/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    //  CREATE JOB
    @Operation(
            summary = "Create a new job position",
            description = "Adds a new job title to the system, including minimum and maximum salary ranges.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Job created successfully",
                            content = @Content(schema = @Schema(implementation = JobResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid job details", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Job title already exists", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobRequest request) {
        JobResponse response = jobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //  GET ALL JOBS
    @Operation(
            summary = "Retrieve all jobs",
            description = "Fetches a list of all available job positions within the company.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of jobs retrieved successfully",
                            content = @Content(schema = @Schema(implementation = JobResponse.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    //  GET JOB BY ID
    @Operation(
            summary = "Get job by ID",
            description = "Retrieves detailed information about a specific job position using its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Job found",
                            content = @Content(schema = @Schema(implementation = JobResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Job not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(
            @Parameter(description = "ID of the job to retrieve") @PathVariable Long id) {
        JobResponse job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    //  UPDATE JOB
    @Operation(
            summary = "Update job information",
            description = "Updates details for an existing job position, such as title or salary range.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Job updated successfully",
                            content = @Content(schema = @Schema(implementation = JobResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid job data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Job not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @Parameter(description = "ID of the job to update") @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {
        JobResponse updated = jobService.updateJob(id, request);
        return ResponseEntity.ok(updated);
    }

    //  DELETE JOB
    @Operation(
            summary = "Delete a job position",
            description = "Removes a job title from the system. Be cautious — employees linked to this job should be reassigned first.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Job deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Job not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @Parameter(description = "ID of the job to delete") @PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
