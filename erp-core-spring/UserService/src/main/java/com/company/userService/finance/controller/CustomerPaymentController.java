package com.company.userService.finance.controller;

import com.company.userService.finance.dto.CustomerPaymentCreateRequest;
import com.company.userService.finance.dto.CustomerPaymentResponse;
import com.company.userService.finance.service.CustomerPaymentService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer-payments")
@AllArgsConstructor
public class CustomerPaymentController {
    private final CustomerPaymentService customerPaymentService;

    @PostMapping
    public ResponseEntity<CustomerPaymentResponse> recordPayment(@RequestBody CustomerPaymentCreateRequest request) {
        return ResponseEntity.ok(customerPaymentService.recordPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerPaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(customerPaymentService.getPaymentById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CustomerPaymentResponse>> getPaymentsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(customerPaymentService.getPaymentsByCompany(companyId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerPaymentResponse>> getPaymentsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerPaymentService.getPaymentsByCustomer(customerId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CustomerPaymentResponse>> getPaymentsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(customerPaymentService.getPaymentsByDateRange(startDate, endDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        customerPaymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
