package com.company.userService.finance.controller;

import com.company.userService.finance.dto.BankAccountCreateRequest;
import com.company.userService.finance.dto.BankAccountResponse;
import com.company.userService.finance.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
@AllArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<BankAccountResponse> createAccount(@RequestBody BankAccountCreateRequest request) {
        return ResponseEntity.ok(bankAccountService.createAccount(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(bankAccountService.getAccountById(id));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<BankAccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankAccountService.getAccountByNumber(accountNumber));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<BankAccountResponse>> getAccountsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(bankAccountService.getAccountsByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<List<BankAccountResponse>> getActiveAccountsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(bankAccountService.getActiveAccountsByCompany(companyId));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Void> depositFunds(@PathVariable Long id, @RequestParam BigDecimal amount) {
        bankAccountService.depositFunds(id, amount);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdrawFunds(@PathVariable Long id, @RequestParam BigDecimal amount) {
        bankAccountService.withdrawFunds(id, amount);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccountResponse> updateAccount(
        @PathVariable Long id,
        @RequestBody BankAccountCreateRequest request) {
        return ResponseEntity.ok(bankAccountService.updateAccount(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        bankAccountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
