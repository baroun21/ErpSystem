package com.company.userService.finance.controller;

import com.company.userService.finance.dto.CostCenterCreateRequest;
import com.company.userService.finance.dto.CostCenterResponse;
import com.company.userService.finance.service.CostCenterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cost-centers")
@AllArgsConstructor
public class CostCenterController {
    private final CostCenterService costCenterService;

    @PostMapping
    public ResponseEntity<CostCenterResponse> createCostCenter(@RequestBody CostCenterCreateRequest request) {
        return ResponseEntity.ok(costCenterService.createCostCenter(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostCenterResponse> getCostCenterById(@PathVariable Long id) {
        return ResponseEntity.ok(costCenterService.getCostCenterById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CostCenterResponse>> getCostCentersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(costCenterService.getCostCentersByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<List<CostCenterResponse>> getActiveCostCentersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(costCenterService.getActiveCostCentersByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostCenterResponse> updateCostCenter(
        @PathVariable Long id,
        @RequestBody CostCenterCreateRequest request) {
        return ResponseEntity.ok(costCenterService.updateCostCenter(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostCenter(@PathVariable Long id) {
        costCenterService.deleteCostCenter(id);
        return ResponseEntity.noContent().build();
    }
}
