package com.company.userService.HrModule.controllers;

import com.company.erp.erp.Dtos.sales.LeadDTO;
import com.company.userService.HrModule.services.LeadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales/leads")
@RequiredArgsConstructor
@Slf4j
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<LeadDTO> createLead(@RequestBody LeadDTO leadDTO) {
        log.info("POST /api/sales/leads - Creating new lead");
        return ResponseEntity.status(HttpStatus.CREATED).body(leadService.createLead(leadDTO));
    }

    @GetMapping("/{companyId}/{leadId}")
    public ResponseEntity<LeadDTO> getLead(@PathVariable Long companyId, @PathVariable Long leadId) {
        log.info("GET /api/sales/leads/{}/{}", companyId, leadId);
        return leadService.getLeadById(companyId, leadId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}/status/{status}")
    public ResponseEntity<List<LeadDTO>> getLeadsByStatus(@PathVariable Long companyId, @PathVariable String status) {
        log.info("GET /api/sales/leads/company/{}/status/{}", companyId, status);
        return ResponseEntity.ok(leadService.getLeadsByCompanyAndStatus(companyId, status));
    }

    @GetMapping("/company/{companyId}/source/{source}")
    public ResponseEntity<List<LeadDTO>> getLeadsBySource(@PathVariable Long companyId, @PathVariable String source) {
        return ResponseEntity.ok(leadService.getLeadsBySource(companyId, source));
    }

    @GetMapping("/company/{companyId}/assigned/{salesRepId}")
    public ResponseEntity<List<LeadDTO>> getAssignedLeads(@PathVariable Long companyId, @PathVariable Long salesRepId) {
        return ResponseEntity.ok(leadService.getAssignedLeads(companyId, salesRepId));
    }

    @GetMapping("/company/{companyId}/high-value")
    public ResponseEntity<List<LeadDTO>> getHighValueLeads(
        @PathVariable Long companyId,
        @RequestParam BigDecimal minValue) {
        return ResponseEntity.ok(leadService.getHighValueLeads(companyId, minValue));
    }

    @GetMapping("/company/{companyId}/date-range")
    public ResponseEntity<List<LeadDTO>> getLeadsByDateRange(
        @PathVariable Long companyId,
        @RequestParam LocalDateTime startDate,
        @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(leadService.getLeadsByDateRange(companyId, startDate, endDate));
    }

    @GetMapping("/company/{companyId}/search")
    public ResponseEntity<List<LeadDTO>> searchByEmail(
        @PathVariable Long companyId,
        @RequestParam String email) {
        return ResponseEntity.ok(leadService.searchLeadsByEmail(companyId, email));
    }

    @PutMapping("/{companyId}/{leadId}")
    public ResponseEntity<LeadDTO> updateLead(
        @PathVariable Long companyId,
        @PathVariable Long leadId,
        @RequestBody LeadDTO leadDTO) {
        log.info("PUT /api/sales/leads/{}/{}", companyId, leadId);
        return ResponseEntity.ok(leadService.updateLead(companyId, leadId, leadDTO));
    }

    @DeleteMapping("/{companyId}/{leadId}")
    public ResponseEntity<Void> deleteLead(@PathVariable Long companyId, @PathVariable Long leadId) {
        log.info("DELETE /api/sales/leads/{}/{}", companyId, leadId);
        leadService.deleteLead(companyId, leadId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}/count-by-status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable Long companyId, @PathVariable String status) {
        return ResponseEntity.ok(leadService.countLeadsByStatus(companyId, status));
    }
}
