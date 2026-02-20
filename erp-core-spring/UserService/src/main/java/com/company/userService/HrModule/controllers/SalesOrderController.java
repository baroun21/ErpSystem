package com.company.userService.HrModule.controllers;

import com.company.erp.erp.Dtos.sales.SalesOrderDTO;
import com.company.userService.HrModule.services.SalesOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales/orders")
@RequiredArgsConstructor
@Slf4j
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<SalesOrderDTO> createSalesOrder(@RequestBody SalesOrderDTO salesOrderDTO) {
        log.info("POST /api/sales/orders - Creating new sales order");
        return ResponseEntity.status(HttpStatus.CREATED).body(salesOrderService.createSalesOrder(salesOrderDTO));
    }

    @GetMapping("/{companyId}/{orderId}")
    public ResponseEntity<SalesOrderDTO> getSalesOrder(@PathVariable String companyId, @PathVariable Long orderId) {
        return salesOrderService.getSalesOrderById(companyId, orderId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}/status/{status}")
    public ResponseEntity<List<SalesOrderDTO>> getSalesOrdersByStatus(@PathVariable String companyId, @PathVariable String status) {
        log.info("GET /api/sales/orders/company/{}/status/{}", companyId, status);
        return ResponseEntity.ok(salesOrderService.getSalesOrdersByStatus(companyId, status));
    }

    @GetMapping("/company/{companyId}/customer/{customerId}")
    public ResponseEntity<List<SalesOrderDTO>> getSalesOrdersByCustomer(@PathVariable String companyId, @PathVariable String customerId) {
        return ResponseEntity.ok(salesOrderService.getSalesOrdersByCustomer(companyId, customerId));
    }

    @GetMapping("/company/{companyId}/date-range")
    public ResponseEntity<List<SalesOrderDTO>> getSalesOrdersByDateRange(
        @PathVariable String companyId,
        @RequestParam LocalDateTime startDate,
        @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(salesOrderService.getSalesOrdersByDateRange(companyId, startDate, endDate));
    }

    @GetMapping("/company/{companyId}/overdue")
    public ResponseEntity<List<SalesOrderDTO>> getOverdueOrders(@PathVariable String companyId) {
        return ResponseEntity.ok(salesOrderService.getOverdueOrders(companyId));
    }

    @GetMapping("/company/{companyId}/unpaid")
    public ResponseEntity<List<SalesOrderDTO>> getUnpaidOrders(@PathVariable String companyId) {
        return ResponseEntity.ok(salesOrderService.getUnpaidOrders(companyId));
    }

    @GetMapping("/company/{companyId}/high-value")
    public ResponseEntity<List<SalesOrderDTO>> getHighValueOrders(
        @PathVariable String companyId,
        @RequestParam BigDecimal minAmount) {
        return ResponseEntity.ok(salesOrderService.getHighValueOrders(companyId, minAmount));
    }

    @GetMapping("/company/{companyId}/opportunity/{opportunityId}")
    public ResponseEntity<List<SalesOrderDTO>> getOrdersByOpportunity(
        @PathVariable String companyId,
        @PathVariable Long opportunityId) {
        return ResponseEntity.ok(salesOrderService.getOrdersByOpportunity(companyId, opportunityId));
    }

    @PutMapping("/{companyId}/{orderId}")
    public ResponseEntity<SalesOrderDTO> updateSalesOrder(
        @PathVariable String companyId,
        @PathVariable Long orderId,
        @RequestBody SalesOrderDTO salesOrderDTO) {
        log.info("PUT /api/sales/orders/{}/{}", companyId, orderId);
        return ResponseEntity.ok(salesOrderService.updateSalesOrder(companyId, orderId, salesOrderDTO));
    }

    @PutMapping("/{companyId}/{orderId}/confirm")
    public ResponseEntity<SalesOrderDTO> confirmOrder(@PathVariable String companyId, @PathVariable Long orderId) {
        log.info("PUT /api/sales/orders/{}/{}/confirm", companyId, orderId);
        return ResponseEntity.ok(salesOrderService.confirmOrder(companyId, orderId));
    }

    @PutMapping("/{companyId}/{orderId}/ship")
    public ResponseEntity<SalesOrderDTO> shipOrder(@PathVariable String companyId, @PathVariable Long orderId) {
        return ResponseEntity.ok(salesOrderService.shipOrder(companyId, orderId));
    }

    @PutMapping("/{companyId}/{orderId}/deliver")
    public ResponseEntity<SalesOrderDTO> deliverOrder(@PathVariable String companyId, @PathVariable Long orderId) {
        return ResponseEntity.ok(salesOrderService.deliverOrder(companyId, orderId));
    }

    @PutMapping("/{companyId}/{orderId}/cancel")
    public ResponseEntity<SalesOrderDTO> cancelOrder(@PathVariable String companyId, @PathVariable Long orderId) {
        return ResponseEntity.ok(salesOrderService.cancelOrder(companyId, orderId));
    }

    @DeleteMapping("/{companyId}/{orderId}")
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable String companyId, @PathVariable Long orderId) {
        log.info("DELETE /api/sales/orders/{}/{}", companyId, orderId);
        salesOrderService.deleteSalesOrder(companyId, orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}/customer/{customerId}/lifetime-value")
    public ResponseEntity<BigDecimal> getCustomerLifetimeValue(
        @PathVariable String companyId,
        @PathVariable String customerId) {
        return ResponseEntity.ok(salesOrderService.calculateCustomerLifetimeValue(companyId, customerId));
    }

    @GetMapping("/company/{companyId}/revenue")
    public ResponseEntity<BigDecimal> getRevenue(
        @PathVariable String companyId,
        @RequestParam LocalDateTime startDate,
        @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(salesOrderService.calculateRevenue(companyId, startDate, endDate));
    }

    @GetMapping("/company/{companyId}/customer/{customerId}/order-count")
    public ResponseEntity<Long> countCustomerOrders(
        @PathVariable String companyId,
        @PathVariable String customerId) {
        return ResponseEntity.ok(salesOrderService.countCustomerOrders(companyId, customerId));
    }
}
