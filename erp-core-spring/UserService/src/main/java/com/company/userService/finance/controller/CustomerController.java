package com.company.userService.finance.controller;

import com.company.userService.finance.dto.CustomerCreateRequest;
import com.company.userService.finance.dto.CustomerResponse;
import com.company.userService.finance.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerCreateRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CustomerResponse>> getCustomersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(customerService.getCustomersByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<List<CustomerResponse>> getActiveCustomersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(customerService.getActiveCustomersByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
        @PathVariable Long id,
        @RequestBody CustomerCreateRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
