package com.company.userService.finance.controller;

import com.company.userService.finance.dto.BillCreateRequest;
import com.company.userService.finance.dto.BillResponse;
import com.company.userService.finance.service.BillService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@AllArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @PostMapping
    public ResponseEntity<BillResponse> createBill(@RequestBody BillCreateRequest request) {
        return ResponseEntity.ok(billService.createBill(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<BillResponse>> getBillsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(billService.getBillsByCompany(companyId));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<BillResponse>> getBillsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(billService.getBillsBySupplier(supplierId));
    }

    @PostMapping("/{id}/post")
    public ResponseEntity<Void> postBill(@PathVariable Long id) {
        billService.postBill(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/payment")
    public ResponseEntity<Void> recordPayment(@PathVariable Long id, @RequestParam BigDecimal amount) {
        billService.recordPayment(id, amount);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillResponse> updateBill(
        @PathVariable Long id,
        @RequestBody BillCreateRequest request) {
        return ResponseEntity.ok(billService.updateBill(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
