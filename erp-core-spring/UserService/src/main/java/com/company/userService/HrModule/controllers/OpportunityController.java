package com.company.userService.HrModule.controllers;

import com.company.erp.erp.Dtos.sales.OpportunityDTO;
import com.company.userService.HrModule.services.OpportunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales/opportunities")
@RequiredArgsConstructor
@Slf4j
public class OpportunityController {

    private final OpportunityService opportunityService;

    @PostMapping
    public ResponseEntity<OpportunityDTO> createOpportunity(@RequestBody OpportunityDTO opportunityDTO) {
        log.info("POST /api/sales/opportunities - Creating new opportunity");
        return ResponseEntity.status(HttpStatus.CREATED).body(opportunityService.createOpportunity(opportunityDTO));
    }

    @GetMapping("/{companyId}/{opportunityId}")
    public ResponseEntity<OpportunityDTO> getOpportunity(@PathVariable Long companyId, @PathVariable Long opportunityId) {
        return opportunityService.getOpportunityById(companyId, opportunityId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}/status/{status}")
    public ResponseEntity<List<OpportunityDTO>> getOpportunitiesByStatus(@PathVariable Long companyId, @PathVariable String status) {
        log.info("GET /api/sales/opportunities/company/{}/status/{}", companyId, status);
        return ResponseEntity.ok(opportunityService.getOpportunitiesByStatus(companyId, status));
    }

    @GetMapping("/company/{companyId}/stage/{stageId}")
    public ResponseEntity<List<OpportunityDTO>> getOpportunitiesByStage(@PathVariable Long companyId, @PathVariable Long stageId) {
        return ResponseEntity.ok(opportunityService.getOpportunitiesByStage(companyId, stageId));
    }

    @GetMapping("/company/{companyId}/lead/{leadId}")
    public ResponseEntity<List<OpportunityDTO>> getOpportunitiesByLead(@PathVariable Long companyId, @PathVariable Long leadId) {
        return ResponseEntity.ok(opportunityService.getOpportunitiesByLead(companyId, leadId));
    }

    @GetMapping("/company/{companyId}/close-date-range")
    public ResponseEntity<List<OpportunityDTO>> getByCloseDateRange(
        @PathVariable Long companyId,
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(opportunityService.getOpportunitiesByCloseDateRange(companyId, startDate, endDate));
    }

    @GetMapping("/company/{companyId}/high-probability")
    public ResponseEntity<List<OpportunityDTO>> getHighProbability(
        @PathVariable Long companyId,
        @RequestParam Integer minProbability) {
        return ResponseEntity.ok(opportunityService.getHighProbabilityOpportunities(companyId, minProbability));
    }

    @GetMapping("/company/{companyId}/forecast")
    public ResponseEntity<List<OpportunityDTO>> getForecastingOpportunities(@PathVariable Long companyId) {
        return ResponseEntity.ok(opportunityService.getForecastingOpportunities(companyId));
    }

    @PutMapping("/{companyId}/{opportunityId}")
    public ResponseEntity<OpportunityDTO> updateOpportunity(
        @PathVariable Long companyId,
        @PathVariable Long opportunityId,
        @RequestBody OpportunityDTO opportunityDTO) {
        log.info("PUT /api/sales/opportunities/{}/{}", companyId, opportunityId);
        return ResponseEntity.ok(opportunityService.updateOpportunity(companyId, opportunityId, opportunityDTO));
    }

    @PutMapping("/{companyId}/{opportunityId}/win")
    public ResponseEntity<OpportunityDTO> winOpportunity(@PathVariable Long companyId, @PathVariable Long opportunityId) {
        log.info("PUT /api/sales/opportunities/{}/{}/win", companyId, opportunityId);
        return ResponseEntity.ok(opportunityService.winOpportunity(companyId, opportunityId));
    }

    @PutMapping("/{companyId}/{opportunityId}/lose")
    public ResponseEntity<OpportunityDTO> loseOpportunity(@PathVariable Long companyId, @PathVariable Long opportunityId) {
        log.info("PUT /api/sales/opportunities/{}/{}/lose", companyId, opportunityId);
        return ResponseEntity.ok(opportunityService.loseOpportunity(companyId, opportunityId));
    }

    @DeleteMapping("/{companyId}/{opportunityId}")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Long companyId, @PathVariable Long opportunityId) {
        log.info("DELETE /api/sales/opportunities/{}/{}", companyId, opportunityId);
        opportunityService.deleteOpportunity(companyId, opportunityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}/revenue-forecast")
    public ResponseEntity<BigDecimal> calculateRevenueForecast(@PathVariable Long companyId) {
        log.info("GET /api/sales/opportunities/company/{}/revenue-forecast", companyId);
        return ResponseEntity.ok(opportunityService.calculateRevenueForecast(companyId));
    }

    @GetMapping("/company/{companyId}/count-open")
    public ResponseEntity<Long> countOpenOpportunities(@PathVariable Long companyId) {
        return ResponseEntity.ok(opportunityService.countOpenOpportunities(companyId));
    }
}
