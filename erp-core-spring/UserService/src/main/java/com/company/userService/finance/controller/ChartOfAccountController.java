package com.company.userService.finance.controller;

import com.company.userService.finance.dto.ChartOfAccountCreateRequest;
import com.company.userService.finance.dto.ChartOfAccountResponse;
import com.company.userService.finance.service.ChartOfAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chart-of-accounts")
@AllArgsConstructor
public class ChartOfAccountController {
    private final ChartOfAccountService chartOfAccountService;

    @PostMapping
    public ResponseEntity<ChartOfAccountResponse> createAccount(@RequestBody ChartOfAccountCreateRequest request) {
        return ResponseEntity.ok(chartOfAccountService.createAccount(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChartOfAccountResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(chartOfAccountService.getAccountById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ChartOfAccountResponse> getAccountByCode(@PathVariable String code) {
        return ResponseEntity.ok(chartOfAccountService.getAccountByCode(code));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ChartOfAccountResponse>> getAccountsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(chartOfAccountService.getAccountsByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/type/{accountType}")
    public ResponseEntity<List<ChartOfAccountResponse>> getAccountsByType(
        @PathVariable Long companyId,
        @PathVariable String accountType) {
        return ResponseEntity.ok(chartOfAccountService.getAccountsByType(companyId, accountType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChartOfAccountResponse> updateAccount(
        @PathVariable Long id,
        @RequestBody ChartOfAccountCreateRequest request) {
        return ResponseEntity.ok(chartOfAccountService.updateAccount(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        chartOfAccountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
