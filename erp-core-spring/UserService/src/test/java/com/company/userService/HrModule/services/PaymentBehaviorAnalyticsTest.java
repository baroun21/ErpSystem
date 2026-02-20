package com.company.userService.HrModule.services;

import com.company.erp.erp.entites.sales.CustomerRiskScore;
import com.company.erp.erp.entites.sales.SalesOrder;
import com.company.userService.HrModule.repositories.CustomerRiskScoreRepository;
import com.company.userService.HrModule.repositories.SalesOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class PaymentBehaviorAnalyticsTest {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private CustomerRiskScoreRepository customerRiskScoreRepository;

    private PaymentBehaviorAnalyticsService paymentBehaviorAnalyticsService;
    private String testCompanyId = "COMP-1";
    private String testCustomerId = "CUST-100";

    @BeforeEach
    void setUp() {
        paymentBehaviorAnalyticsService = new PaymentBehaviorAnalyticsService(salesOrderRepository, customerRiskScoreRepository);
    }

    @Test
    void testCalculateCompanyDSO() {
        SalesOrder order = SalesOrder.builder()
            .companyId(testCompanyId)
            .orderNumber("SO-100")
            .customerId(testCustomerId)
            .customerName("Acme Corp")
            .orderDate(LocalDateTime.now().minusDays(10))
            .requiredDeliveryDate(LocalDateTime.now().minusDays(2))
            .totalAmount(BigDecimal.valueOf(1000))
            .status("CONFIRMED")
            .paymentStatus("UNPAID")
            .createdAt(LocalDateTime.now())
            .build();
        salesOrderRepository.save(order);

        assertNotNull(paymentBehaviorAnalyticsService.calculateCompanyDSO(testCompanyId, 30));
    }

    @Test
    void testGetPaymentRiskCustomers() {
        CustomerRiskScore score = CustomerRiskScore.builder()
            .companyId(testCompanyId)
            .customerId(testCustomerId)
            .customerName("Acme Corp")
            .riskLevel("HIGH")
            .overallRiskScore(BigDecimal.valueOf(80))
            .status("ACTIVE")
            .createdAt(LocalDateTime.now())
            .build();
        customerRiskScoreRepository.save(score);

        List<CustomerRiskScore> results = paymentBehaviorAnalyticsService.getPaymentRiskCustomers(testCompanyId);
        assertNotNull(results);
    }
}
