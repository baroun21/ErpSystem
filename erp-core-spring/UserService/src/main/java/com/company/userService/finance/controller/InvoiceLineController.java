package com.company.userService.finance.controller;

import com.company.userService.finance.dto.InvoiceLineCreateRequest;
import com.company.userService.finance.dto.InvoiceLineResponse;
import com.company.userService.finance.service.InvoiceLineService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-lines")
@AllArgsConstructor
public class InvoiceLineController {
    private final InvoiceLineService invoiceLineService;

    @PostMapping
    public ResponseEntity<InvoiceLineResponse> createInvoiceLine(@RequestBody InvoiceLineCreateRequest request) {
        return ResponseEntity.ok(invoiceLineService.createInvoiceLine(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceLineResponse> getInvoiceLineById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceLineService.getInvoiceLineById(id));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<InvoiceLineResponse>> getInvoiceLinesByInvoice(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(invoiceLineService.getInvoiceLinesByInvoice(invoiceId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceLineResponse> updateInvoiceLine(
        @PathVariable Long id,
        @RequestBody InvoiceLineCreateRequest request) {
        return ResponseEntity.ok(invoiceLineService.updateInvoiceLine(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceLine(@PathVariable Long id) {
        invoiceLineService.deleteInvoiceLine(id);
        return ResponseEntity.noContent().build();
    }
}
