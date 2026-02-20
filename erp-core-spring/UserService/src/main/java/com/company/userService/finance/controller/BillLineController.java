package com.company.userService.finance.controller;

import com.company.userService.finance.dto.BillLineCreateRequest;
import com.company.userService.finance.dto.BillLineResponse;
import com.company.userService.finance.service.BillLineService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill-lines")
@AllArgsConstructor
public class BillLineController {
    private final BillLineService billLineService;

    @PostMapping
    public ResponseEntity<BillLineResponse> createBillLine(@RequestBody BillLineCreateRequest request) {
        return ResponseEntity.ok(billLineService.createBillLine(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillLineResponse> getBillLineById(@PathVariable Long id) {
        return ResponseEntity.ok(billLineService.getBillLineById(id));
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<List<BillLineResponse>> getBillLinesByBill(@PathVariable Long billId) {
        return ResponseEntity.ok(billLineService.getBillLinesByBill(billId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillLineResponse> updateBillLine(
        @PathVariable Long id,
        @RequestBody BillLineCreateRequest request) {
        return ResponseEntity.ok(billLineService.updateBillLine(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillLine(@PathVariable Long id) {
        billLineService.deleteBillLine(id);
        return ResponseEntity.noContent().build();
    }
}
