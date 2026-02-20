package com.company.userService.HrModule.controllers;

import com.company.erp.erp.entites.sales.CustomerRiskScore;
import com.company.erp.erp.entites.sales.SalesOrder;
import com.company.userService.HrModule.services.PaymentBehaviorAnalyticsService;
import com.company.userService.HrModule.services.ProfitAnalyticsService;
import com.company.userService.HrModule.services.RevenueAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales/analytics")
@RequiredArgsConstructor
@Slf4j
public class SalesAnalyticsController {

    private final RevenueAnalyticsService revenueAnalyticsService;
    private final ProfitAnalyticsService profitAnalyticsService;
    private final PaymentBehaviorAnalyticsService paymentBehaviorAnalyticsService;

    @GetMapping("/company/{companyId}/revenue-forecast")
    public ResponseEntity<BigDecimal> calculateRevenueForecast(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.calculateTotalRevenueForecast(companyId));
    }

    @GetMapping("/company/{companyId}/forecast-by-stage")
    public ResponseEntity<Map<String, BigDecimal>> getForecastByStage(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.getForecastByStage(companyId));
    }

    @GetMapping("/company/{companyId}/forecast-detailed")
    public ResponseEntity<Map<String, Object>> getDetailedForecast(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.getDetailedForecastByStage(companyId));
    }

    @GetMapping("/company/{companyId}/forecast-by-month")
    public ResponseEntity<Map<String, BigDecimal>> getForecastByMonth(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.getForecastByMonth(companyId));
    }

    @GetMapping("/company/{companyId}/best-case-revenue")
    public ResponseEntity<BigDecimal> getBestCaseRevenue(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.getBestCaseRevenue(companyId));
    }

    @GetMapping("/company/{companyId}/worst-case-revenue")
    public ResponseEntity<BigDecimal> getWorstCaseRevenue(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.getWorstCaseRevenue(companyId));
    }

    @GetMapping("/company/{companyId}/most-likely-revenue")
    public ResponseEntity<BigDecimal> getMostLikelyRevenue(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.getMostLikelyRevenue(companyId));
    }

    @GetMapping("/company/{companyId}/average-deal-size")
    public ResponseEntity<BigDecimal> getAverageDealSize(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.getAverageDealSize(companyId));
    }

    @GetMapping("/company/{companyId}/forecast-concentration")
    public ResponseEntity<BigDecimal> getForecastConcentration(@PathVariable String companyId) {
        return ResponseEntity.ok(revenueAnalyticsService.getForecastConcentration(companyId));
    }

    @GetMapping("/company/{companyId}/customer/{customerId}/profit-analysis")
    public ResponseEntity<Map<String, Object>> getCustomerProfitAnalysis(
        @PathVariable String companyId,
        @PathVariable String customerId) {
        return ResponseEntity.ok(profitAnalyticsService.getCustomerProfitAnalysis(companyId, customerId));
    }

    @GetMapping("/company/{companyId}/top-profitable-customers")
    public ResponseEntity<List<Map<String, Object>>> getTopProfitableCustomers(
        @PathVariable String companyId,
        @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(profitAnalyticsService.getTopProfitableCustomers(companyId, limit));
    }

    @GetMapping("/company/{companyId}/profitability-summary")
    public ResponseEntity<Map<String, Object>> getProfitabilitySummary(@PathVariable String companyId) {
        return ResponseEntity.ok(profitAnalyticsService.getProfitabilitySummary(companyId));
    }

    @GetMapping("/company/{companyId}/high-margin-sales")
    public ResponseEntity<List<SalesOrder>> getHighMarginSales(
        @PathVariable String companyId,
        @RequestParam(defaultValue = "20") BigDecimal minMargin) {
        return ResponseEntity.ok(profitAnalyticsService.getHighMarginSales(companyId, minMargin));
    }

    @GetMapping("/company/{companyId}/dso")
    public ResponseEntity<Map<String, Object>> getCompanyDSO(
        @PathVariable String companyId,
        @RequestParam(defaultValue = "90") int days) {
        return ResponseEntity.ok(paymentBehaviorAnalyticsService.calculateCompanyDSO(companyId, days));
    }

    @GetMapping("/company/{companyId}/dso-by-customer")
    public ResponseEntity<List<Map<String, Object>>> getDSOByCustomer(
        @PathVariable String companyId,
        @RequestParam(defaultValue = "45") BigDecimal minDSO) {
        return ResponseEntity.ok(paymentBehaviorAnalyticsService.getDSOByCustomer(companyId, minDSO));
    }

    @GetMapping("/company/{companyId}/overdue-summary")
    public ResponseEntity<Map<String, Object>> getOverdueSummary(@PathVariable String companyId) {
        return ResponseEntity.ok(paymentBehaviorAnalyticsService.getOverdueSummary(companyId));
    }

    @GetMapping("/company/{companyId}/customer/{customerId}/payment-history")
    public ResponseEntity<List<SalesOrder>> getPaymentHistory(
        @PathVariable String companyId,
        @PathVariable String customerId,
        @RequestParam(defaultValue = "12") int months) {
        return ResponseEntity.ok(paymentBehaviorAnalyticsService.getPaymentHistory(companyId, customerId, months));
    }

    @GetMapping("/company/{companyId}/payment-patterns")
    public ResponseEntity<Map<String, Object>> getPaymentPatterns(@PathVariable String companyId) {
        return ResponseEntity.ok(paymentBehaviorAnalyticsService.analyzePaymentPatterns(companyId));
    }

    @GetMapping("/company/{companyId}/payment-risk-customers")
    public ResponseEntity<List<CustomerRiskScore>> getPaymentRiskCustomers(@PathVariable String companyId) {
        return ResponseEntity.ok(paymentBehaviorAnalyticsService.getPaymentRiskCustomers(companyId));
    }
}
