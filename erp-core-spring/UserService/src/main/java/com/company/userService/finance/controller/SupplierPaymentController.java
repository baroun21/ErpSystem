package com.company.userService.finance.controller;

import com.company.userService.finance.dto.SupplierPaymentCreateRequest;
import com.company.userService.finance.dto.SupplierPaymentResponse;
import com.company.userService.finance.service.SupplierPaymentService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-payments")
@AllArgsConstructor
public class SupplierPaymentController {
    private final SupplierPaymentService supplierPaymentService;

    @PostMapping
    public ResponseEntity<SupplierPaymentResponse> recordPayment(@RequestBody SupplierPaymentCreateRequest request) {
        return ResponseEntity.ok(supplierPaymentService.recordPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierPaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierPaymentService.getPaymentById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<SupplierPaymentResponse>> getPaymentsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(supplierPaymentService.getPaymentsByCompany(companyId));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierPaymentResponse>> getPaymentsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierPaymentService.getPaymentsBySupplier(supplierId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<SupplierPaymentResponse>> getPaymentsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(supplierPaymentService.getPaymentsByDateRange(startDate, endDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        supplierPaymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
