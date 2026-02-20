package com.company.userService.HrModule.services;

import com.company.erp.erp.entites.sales.CustomerRiskScore;
import com.company.erp.erp.entites.sales.SalesOrder;
import com.company.userService.HrModule.repositories.CustomerRiskScoreRepository;
import com.company.userService.HrModule.repositories.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentBehaviorAnalyticsService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRiskScoreRepository customerRiskScoreRepository;

    public Map<String, Object> calculateCompanyDSO(String companyId, int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        BigDecimal totalRevenue = salesOrderRepository.calculateRevenue(companyId, startDate, endDate);
        BigDecimal totalAR = salesOrderRepository.findUnpaidOrders(companyId).stream()
            .map(order -> defaultZero(order.getTotalAmount()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal dailySales = (totalRevenue == null || totalRevenue.compareTo(BigDecimal.ZERO) == 0)
            ? BigDecimal.ZERO
            : totalRevenue.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
        BigDecimal dso = dailySales.compareTo(BigDecimal.ZERO) == 0
            ? BigDecimal.ZERO
            : totalAR.divide(dailySales, 2, RoundingMode.HALF_UP);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("days", days);
        result.put("totalAR", totalAR);
        result.put("dailySales", dailySales);
        result.put("dso", dso);
        return result;
    }

    public List<Map<String, Object>> getDSOByCustomer(String companyId, BigDecimal minDSO) {
        List<CustomerRiskScore> scores = customerRiskScoreRepository.findByCompanyAndStatus(companyId, "ACTIVE");
        List<Map<String, Object>> results = new ArrayList<>();
        for (CustomerRiskScore score : scores) {
            BigDecimal dso = defaultZero(score.getDaysSalesOutstanding());
            if (dso.compareTo(minDSO) < 0) {
                continue;
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("customerId", score.getCustomerId());
            item.put("customerName", score.getCustomerName());
            item.put("dso", dso);
            item.put("riskLevel", score.getRiskLevel());
            results.add(item);
        }
        return results;
    }

    public Map<String, Object> getOverdueSummary(String companyId) {
        List<SalesOrder> overdue = salesOrderRepository.findOverdueOrders(companyId);
        int bucket0to30 = 0;
        int bucket31to60 = 0;
        int bucket61to90 = 0;
        int bucket90plus = 0;
        BigDecimal totalOverdue = BigDecimal.ZERO;
        for (SalesOrder order : overdue) {
            totalOverdue = totalOverdue.add(defaultZero(order.getTotalAmount()));
            if (order.getRequiredDeliveryDate() == null) {
                continue;
            }
            long daysOverdue = Duration.between(order.getRequiredDeliveryDate(), LocalDateTime.now()).toDays();
            if (daysOverdue <= 30) {
                bucket0to30++;
            } else if (daysOverdue <= 60) {
                bucket31to60++;
            } else if (daysOverdue <= 90) {
                bucket61to90++;
            } else {
                bucket90plus++;
            }
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("count", overdue.size());
        result.put("totalOverdue", totalOverdue);
        result.put("bucket0to30", bucket0to30);
        result.put("bucket31to60", bucket31to60);
        result.put("bucket61to90", bucket61to90);
        result.put("bucket90plus", bucket90plus);
        return result;
    }

    public List<SalesOrder> getPaymentHistory(String companyId, String customerId, int months) {
        return salesOrderRepository.findByCompanyAndCustomer(companyId, customerId);
    }

    public Map<String, Object> analyzePaymentPatterns(String companyId) {
        List<SalesOrder> orders = salesOrderRepository.findByCompanyId(companyId);
        int early = 0;
        int onTime = 0;
        int late = 0;
        for (SalesOrder order : orders) {
            LocalDateTime dueDate = order.getRequiredDeliveryDate();
            LocalDateTime actualDate = order.getActualDeliveryDate();
            if (dueDate == null || actualDate == null) {
                continue;
            }
            long daysDiff = Duration.between(dueDate, actualDate).toDays();
            if (daysDiff < 0) {
                early++;
            } else if (daysDiff <= 3) {
                onTime++;
            } else {
                late++;
            }
        }
        int total = early + onTime + late;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("early", percent(early, total));
        result.put("onTime", percent(onTime, total));
        result.put("late", percent(late, total));
        return result;
    }

    public List<CustomerRiskScore> getPaymentRiskCustomers(String companyId) {
        return customerRiskScoreRepository.findAtRiskCustomers(companyId);
    }

    private BigDecimal percent(int value, int total) {
        if (total == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(value)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
