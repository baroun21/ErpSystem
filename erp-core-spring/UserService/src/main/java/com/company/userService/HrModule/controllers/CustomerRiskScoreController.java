package com.company.userService.HrModule.controllers;

import com.company.erp.erp.Dtos.sales.CustomerRiskScoreDTO;
import com.company.userService.HrModule.services.CustomerRiskScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sales/risk-scores")
@RequiredArgsConstructor
@Slf4j
public class CustomerRiskScoreController {

    private final CustomerRiskScoreService customerRiskScoreService;

    @PostMapping
    public ResponseEntity<CustomerRiskScoreDTO> createRiskScore(@RequestBody CustomerRiskScoreDTO riskScoreDTO) {
        log.info("POST /api/sales/risk-scores - Creating new risk score");
        return ResponseEntity.status(HttpStatus.CREATED).body(customerRiskScoreService.createRiskScore(riskScoreDTO));
    }

    @GetMapping("/{companyId}/{riskScoreId}")
    public ResponseEntity<CustomerRiskScoreDTO> getRiskScore(@PathVariable String companyId, @PathVariable Long riskScoreId) {
        return customerRiskScoreService.getRiskScoreById(companyId, riskScoreId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}/customer/{customerId}")
    public ResponseEntity<CustomerRiskScoreDTO> getRiskScoreByCustomer(@PathVariable String companyId, @PathVariable String customerId) {
        return customerRiskScoreService.getRiskScoreByCustomer(companyId, customerId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}/risk-level/{riskLevel}")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getRiskScoresByLevel(
        @PathVariable String companyId,
        @PathVariable String riskLevel) {
        log.info("GET /api/sales/risk-scores/company/{}/risk-level/{}", companyId, riskLevel);
        return ResponseEntity.ok(customerRiskScoreService.getRiskScoresByLevel(companyId, riskLevel));
    }

    @GetMapping("/company/{companyId}/at-risk")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getAtRiskCustomers(@PathVariable String companyId) {
        return ResponseEntity.ok(customerRiskScoreService.getAtRiskCustomers(companyId));
    }

    @GetMapping("/company/{companyId}/high-dso")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getHighDSOCustomers(
        @PathVariable String companyId,
        @RequestParam(defaultValue = "60") BigDecimal dsoThreshold) {
        return ResponseEntity.ok(customerRiskScoreService.getHighDSOCustomers(companyId, dsoThreshold));
    }

    @GetMapping("/company/{companyId}/late-payers")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getLatePayerCustomers(@PathVariable String companyId) {
        return ResponseEntity.ok(customerRiskScoreService.getLatePayerCustomers(companyId));
    }

    @GetMapping("/company/{companyId}/with-overdue")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getCustomersWithOverdue(@PathVariable String companyId) {
        return ResponseEntity.ok(customerRiskScoreService.getCustomersWithOverdue(companyId));
    }

    @GetMapping("/company/{companyId}/low-credit")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getLowCreditScoreCustomers(
        @PathVariable String companyId,
        @RequestParam(defaultValue = "500") Integer minCredit) {
        return ResponseEntity.ok(customerRiskScoreService.getLowCreditScoreCustomers(companyId, minCredit));
    }

    @GetMapping("/company/{companyId}/credit-limit-exceeded")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getCreditLimitExceeded(@PathVariable String companyId) {
        return ResponseEntity.ok(customerRiskScoreService.getCreditLimitExceeded(companyId));
    }

    @GetMapping("/company/{companyId}/review-due")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getReviewDueCustomers(@PathVariable String companyId) {
        return ResponseEntity.ok(customerRiskScoreService.getReviewDueCustomers(companyId));
    }

    @GetMapping("/company/{companyId}/status/{status}")
    public ResponseEntity<List<CustomerRiskScoreDTO>> getRiskScoresByStatus(
        @PathVariable String companyId,
        @PathVariable String status) {
        return ResponseEntity.ok(customerRiskScoreService.getRiskScoresByStatus(companyId, status));
    }

    @PutMapping("/{companyId}/{riskScoreId}")
    public ResponseEntity<CustomerRiskScoreDTO> updateRiskScore(
        @PathVariable String companyId,
        @PathVariable Long riskScoreId,
        @RequestBody CustomerRiskScoreDTO riskScoreDTO) {
        log.info("PUT /api/sales/risk-scores/{}/{}", companyId, riskScoreId);
        return ResponseEntity.ok(customerRiskScoreService.updateRiskScore(companyId, riskScoreId, riskScoreDTO));
    }

    @PutMapping("/company/{companyId}/customer/{customerId}/recalculate")
    public ResponseEntity<CustomerRiskScoreDTO> recalculateRiskScore(
        @PathVariable String companyId,
        @PathVariable String customerId) {
        log.info("PUT /api/sales/risk-scores/company/{}/customer/{}/recalculate", companyId, customerId);
        return ResponseEntity.ok(customerRiskScoreService.calculateAndUpdateRiskScore(companyId, customerId));
    }

    @DeleteMapping("/{companyId}/{riskScoreId}")
    public ResponseEntity<Void> deleteRiskScore(@PathVariable String companyId, @PathVariable Long riskScoreId) {
        log.info("DELETE /api/sales/risk-scores/{}/{}", companyId, riskScoreId);
        customerRiskScoreService.deleteRiskScore(companyId, riskScoreId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}/total-risk-exposure")
    public ResponseEntity<BigDecimal> getTotalRiskExposure(@PathVariable String companyId) {
        return ResponseEntity.ok(customerRiskScoreService.calculateTotalRiskExposure(companyId));
    }
}
