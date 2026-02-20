package com.company.userService.HrModule.services;

import com.company.erp.erp.entites.sales.SalesOrder;
import com.company.userService.HrModule.repositories.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProfitAnalyticsService {

    private final SalesOrderRepository salesOrderRepository;

    public Map<String, Object> getCustomerProfitAnalysis(String companyId, String customerId) {
        List<SalesOrder> orders = salesOrderRepository.findByCompanyAndCustomer(companyId, customerId);
        BigDecimal totalRevenue = orders.stream()
            .map(order -> defaultZero(order.getTotalAmount()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDiscount = orders.stream()
            .map(order -> defaultZero(order.getDiscountAmount()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        int orderCount = orders.size();
        BigDecimal avgOrderValue = orderCount == 0
            ? BigDecimal.ZERO
            : totalRevenue.divide(BigDecimal.valueOf(orderCount), 2, RoundingMode.HALF_UP);
        BigDecimal discountPct = totalRevenue.compareTo(BigDecimal.ZERO) == 0
            ? BigDecimal.ZERO
            : totalDiscount.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("customerId", customerId);
        result.put("totalRevenue", totalRevenue);
        result.put("orderCount", orderCount);
        result.put("averageOrderValue", avgOrderValue);
        result.put("totalDiscount", totalDiscount);
        result.put("discountPercentage", discountPct);
        return result;
    }

    public List<Map<String, Object>> getTopProfitableCustomers(String companyId, int limit) {
        List<Object[]> rows = salesOrderRepository.findTopCustomersByRevenue(companyId);
        List<Map<String, Object>> results = new ArrayList<>();
        for (Object[] row : rows) {
            if (results.size() >= limit) {
                break;
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("customerId", row[0]);
            item.put("totalRevenue", row[1]);
            results.add(item);
        }
        return results;
    }

    public Map<String, Object> getProfitabilitySummary(String companyId) {
        List<SalesOrder> orders = salesOrderRepository.findByCompanyId(companyId);
        BigDecimal totalRevenue = orders.stream()
            .map(order -> defaultZero(order.getTotalAmount()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDiscount = orders.stream()
            .map(order -> defaultZero(order.getDiscountAmount()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        int orderCount = orders.size();
        BigDecimal avgOrderValue = orderCount == 0
            ? BigDecimal.ZERO
            : totalRevenue.divide(BigDecimal.valueOf(orderCount), 2, RoundingMode.HALF_UP);
        BigDecimal discountPct = totalRevenue.compareTo(BigDecimal.ZERO) == 0
            ? BigDecimal.ZERO
            : totalDiscount.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("totalDiscount", totalDiscount);
        result.put("averageOrderValue", avgOrderValue);
        result.put("discountPercentage", discountPct);
        return result;
    }

    public List<SalesOrder> getHighMarginSales(String companyId, BigDecimal minMarginPercent) {
        List<SalesOrder> orders = salesOrderRepository.findByCompanyId(companyId);
        List<SalesOrder> results = new ArrayList<>();
        for (SalesOrder order : orders) {
            BigDecimal total = defaultZero(order.getTotalAmount());
            BigDecimal discount = defaultZero(order.getDiscountAmount());
            if (total.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            BigDecimal marginPercent = BigDecimal.valueOf(100).subtract(
                discount.multiply(BigDecimal.valueOf(100)).divide(total, 2, RoundingMode.HALF_UP)
            );
            if (marginPercent.compareTo(minMarginPercent) >= 0) {
                results.add(order);
            }
        }
        return results;
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
