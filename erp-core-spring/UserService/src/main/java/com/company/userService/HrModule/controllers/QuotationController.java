package com.company.userService.HrModule.controllers;

import com.company.erp.erp.Dtos.sales.QuotationDTO;
import com.company.userService.HrModule.services.QuotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales/quotations")
@RequiredArgsConstructor
@Slf4j
public class QuotationController {

    private final QuotationService quotationService;

    @PostMapping
    public ResponseEntity<QuotationDTO> createQuotation(@RequestBody QuotationDTO quotationDTO) {
        log.info("POST /api/sales/quotations - Creating new quotation");
        return ResponseEntity.status(HttpStatus.CREATED).body(quotationService.createQuotation(quotationDTO));
    }

    @GetMapping("/{companyId}/{quotationId}")
    public ResponseEntity<QuotationDTO> getQuotation(@PathVariable Long companyId, @PathVariable Long quotationId) {
        return quotationService.getQuotationById(companyId, quotationId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}/status/{status}")
    public ResponseEntity<List<QuotationDTO>> getQuotationsByStatus(@PathVariable Long companyId, @PathVariable String status) {
        log.info("GET /api/sales/quotations/company/{}/status/{}", companyId, status);
        return ResponseEntity.ok(quotationService.getQuotationsByStatus(companyId, status));
    }

    @GetMapping("/company/{companyId}/lead/{leadId}")
    public ResponseEntity<List<QuotationDTO>> getQuotationsByLead(@PathVariable Long companyId, @PathVariable Long leadId) {
        return ResponseEntity.ok(quotationService.getQuotationsByLead(companyId, leadId));
    }

    @GetMapping("/company/{companyId}/draft")
    public ResponseEntity<List<QuotationDTO>> getDraftQuotes(@PathVariable Long companyId) {
        return ResponseEntity.ok(quotationService.getDraftQuotes(companyId));
    }

    @GetMapping("/company/{companyId}/approved")
    public ResponseEntity<List<QuotationDTO>> getApprovedQuotes(@PathVariable Long companyId) {
        return ResponseEntity.ok(quotationService.getApprovedQuotes(companyId));
    }

    @GetMapping("/company/{companyId}/expired")
    public ResponseEntity<List<QuotationDTO>> getExpiredQuotes(@PathVariable Long companyId) {
        return ResponseEntity.ok(quotationService.getExpiredQuotes(companyId));
    }

    @GetMapping("/company/{companyId}/convertible")
    public ResponseEntity<List<QuotationDTO>> getConvertibleQuotes(@PathVariable Long companyId) {
        return ResponseEntity.ok(quotationService.getConvertibleQuotes(companyId));
    }

    @GetMapping("/company/{companyId}/date-range")
    public ResponseEntity<List<QuotationDTO>> getQuotationsByDateRange(
        @PathVariable Long companyId,
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(quotationService.getQuotationsByDateRange(companyId, startDate, endDate));
    }

    @PutMapping("/{companyId}/{quotationId}")
    public ResponseEntity<QuotationDTO> updateQuotation(
        @PathVariable Long companyId,
        @PathVariable Long quotationId,
        @RequestBody QuotationDTO quotationDTO) {
        log.info("PUT /api/sales/quotations/{}/{}", companyId, quotationId);
        return ResponseEntity.ok(quotationService.updateQuotation(companyId, quotationId, quotationDTO));
    }

    @PutMapping("/{companyId}/{quotationId}/send")
    public ResponseEntity<QuotationDTO> sendQuotation(@PathVariable Long companyId, @PathVariable Long quotationId) {
        log.info("PUT /api/sales/quotations/{}/{}/send", companyId, quotationId);
        return ResponseEntity.ok(quotationService.sendQuotation(companyId, quotationId));
    }

    @PutMapping("/{companyId}/{quotationId}/approve")
    public ResponseEntity<QuotationDTO> approveQuotation(
        @PathVariable Long companyId,
        @PathVariable Long quotationId,
        @RequestParam Long approvedBy) {
        return ResponseEntity.ok(quotationService.approveQuotation(companyId, quotationId, approvedBy));
    }

    @PutMapping("/{companyId}/{quotationId}/reject")
    public ResponseEntity<QuotationDTO> rejectQuotation(@PathVariable Long companyId, @PathVariable Long quotationId) {
        return ResponseEntity.ok(quotationService.rejectQuotation(companyId, quotationId));
    }

    @PutMapping("/{companyId}/{quotationId}/convert-to-order")
    public ResponseEntity<Long> convertToSalesOrder(
        @PathVariable Long companyId,
        @PathVariable Long quotationId,
        @RequestParam Long salesOrderId) {
        return ResponseEntity.ok(quotationService.convertQuotationToSalesOrder(companyId, quotationId, salesOrderId));
    }

    @DeleteMapping("/{companyId}/{quotationId}")
    public ResponseEntity<Void> deleteQuotation(@PathVariable Long companyId, @PathVariable Long quotationId) {
        log.info("DELETE /api/sales/quotations/{}/{}", companyId, quotationId);
        quotationService.deleteQuotation(companyId, quotationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}/accepted-value")
    public ResponseEntity<BigDecimal> getAcceptedQuotesValue(@PathVariable Long companyId) {
        return ResponseEntity.ok(quotationService.calculateAcceptedQuotesValue(companyId));
    }

    @GetMapping("/company/{companyId}/count-by-status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable Long companyId, @PathVariable String status) {
        return ResponseEntity.ok(quotationService.countByStatus(companyId, status));
    }
}
