package com.company.userService.finance.controller;

import com.company.userService.finance.dto.SupplierCreateRequest;
import com.company.userService.finance.dto.SupplierResponse;
import com.company.userService.finance.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@AllArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@RequestBody SupplierCreateRequest request) {
        return ResponseEntity.ok(supplierService.createSupplier(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<SupplierResponse>> getSuppliersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(supplierService.getSuppliersByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<List<SupplierResponse>> getActiveSuppliersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(supplierService.getActiveSuppliersByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
        @PathVariable Long id,
        @RequestBody SupplierCreateRequest request) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
