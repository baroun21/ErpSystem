package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.SalesOrderDTO;
import com.company.erp.erp.entites.sales.SalesOrder;
import com.company.erp.mapper.sales.SalesOrderMapper;
import com.company.userService.HrModule.repositories.SalesOrderRepository;
import com.company.userService.HrModule.services.impl.SalesOrderServiceImpl;
import org.mapstruct.factory.Mappers;
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
public class SalesOrderServiceTest {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    private SalesOrderService salesOrderService;
    private SalesOrderMapper salesOrderMapper;
    private String testCompanyId = "COMP-1";
    private String testCustomerId = "CUST-100";

    @BeforeEach
    void setUp() {
        salesOrderMapper = Mappers.getMapper(SalesOrderMapper.class);
        salesOrderService = new SalesOrderServiceImpl(salesOrderRepository, salesOrderMapper);
    }

    @Test
    void testCreateSalesOrder() {
        // Arrange
        SalesOrderDTO orderDTO = SalesOrderDTO.builder()
            .companyId(testCompanyId)
            .orderNumber("SO-001")
            .customerId(testCustomerId)
            .customerName("Acme Corp")
            .orderDate(LocalDateTime.now())
            .totalAmount(BigDecimal.valueOf(10000))
            .status("PENDING")
            .paymentStatus("UNPAID")
            .build();

        // Act
        SalesOrderDTO created = salesOrderService.createSalesOrder(orderDTO);

        // Assert
        assertNotNull(created);
        assertNotNull(created.getSalesOrderId());
        assertEquals("SO-001", created.getOrderNumber());
        assertEquals("PENDING", created.getStatus());
    }

    @Test
    void testConfirmOrder() {
        // Arrange
        SalesOrder order = SalesOrder.builder()
            .companyId(testCompanyId)
            .orderNumber("SO-002")
            .customerId(testCustomerId)
            .customerName("Acme Corp")
            .orderDate(LocalDateTime.now())
            .status("PENDING")
            .paymentStatus("UNPAID")
            .totalAmount(BigDecimal.valueOf(5000))
            .createdAt(LocalDateTime.now())
            .build();
        SalesOrder saved = salesOrderRepository.save(order);

        // Act
        SalesOrderDTO confirmed = salesOrderService.confirmOrder(testCompanyId, saved.getSalesOrderId());

        // Assert
        assertEquals("CONFIRMED", confirmed.getStatus());
    }

    @Test
    void testDeliverOrder() {
        // Arrange
        SalesOrder order = SalesOrder.builder()
            .companyId(testCompanyId)
            .orderNumber("SO-003")
            .customerId(testCustomerId)
            .customerName("Acme Corp")
            .orderDate(LocalDateTime.now())
            .status("SHIPPED")
            .paymentStatus("UNPAID")
            .createdAt(LocalDateTime.now())
            .totalAmount(BigDecimal.valueOf(3000))
            .build();
        SalesOrder saved = salesOrderRepository.save(order);

        // Act
        SalesOrderDTO delivered = salesOrderService.deliverOrder(testCompanyId, saved.getSalesOrderId());

        // Assert
        assertEquals("DELIVERED", delivered.getStatus());
        assertNotNull(delivered.getActualDeliveryDate());
    }

    @Test
    void testCalculateCustomerLifetimeValue() {
        // Arrange
        SalesOrder order1 = SalesOrder.builder()
            .companyId(testCompanyId)
            .customerId(testCustomerId)
            .customerName("Acme Corp")
            .orderNumber("SO-004")
            .totalAmount(BigDecimal.valueOf(5000))
            .createdAt(LocalDateTime.now())
            .orderDate(LocalDateTime.now())
            .status("DELIVERED")
            .paymentStatus("PAID")
            .build();
        SalesOrder order2 = SalesOrder.builder()
            .companyId(testCompanyId)
            .customerId(testCustomerId)
            .customerName("Acme Corp")
            .orderNumber("SO-005")
            .totalAmount(BigDecimal.valueOf(3000))
            .createdAt(LocalDateTime.now())
            .orderDate(LocalDateTime.now())
            .status("DELIVERED")
            .paymentStatus("PAID")
            .build();
        salesOrderRepository.saveAll(List.of(order1, order2));

        // Act
        BigDecimal lifetime = salesOrderService.calculateCustomerLifetimeValue(testCompanyId, testCustomerId);

        // Assert
        assertEquals(BigDecimal.valueOf(8000), lifetime);
    }

    @Test
    void testGetHighValueOrders() {
        // Arrange
        SalesOrder highValue = SalesOrder.builder()
            .companyId(testCompanyId)
            .customerId(testCustomerId)
            .customerName("Acme Corp")
            .orderNumber("SO-006")
            .totalAmount(BigDecimal.valueOf(50000))
            .createdAt(LocalDateTime.now())
            .orderDate(LocalDateTime.now())
            .paymentStatus("UNPAID")
            .build();
        SalesOrder lowValue = SalesOrder.builder()
            .companyId(testCompanyId)
            .customerId("CUST-101")
            .customerName("Beta Corp")
            .orderNumber("SO-007")
            .totalAmount(BigDecimal.valueOf(500))
            .createdAt(LocalDateTime.now())
            .orderDate(LocalDateTime.now())
            .paymentStatus("UNPAID")
            .build();
        salesOrderRepository.saveAll(List.of(highValue, lowValue));

        // Act
        List<SalesOrderDTO> results = salesOrderService.getHighValueOrders(testCompanyId, BigDecimal.valueOf(10000));

        // Assert
        assertEquals(1, results.size());
        assertEquals("SO-006", results.get(0).getOrderNumber());
    }

    @Test
    void testCancelOrder() {
        // Arrange
        SalesOrder order = SalesOrder.builder()
            .companyId(testCompanyId)
            .orderNumber("SO-008")
            .customerId(testCustomerId)
            .status("PENDING")
            .createdAt(LocalDateTime.now())
            .totalAmount(BigDecimal.valueOf(2000))
            .customerName("Acme Corp")
            .orderDate(LocalDateTime.now())
            .paymentStatus("UNPAID")
            .build();
        SalesOrder saved = salesOrderRepository.save(order);

        // Act
        SalesOrderDTO cancelled = salesOrderService.cancelOrder(testCompanyId, saved.getSalesOrderId());

        // Assert
        assertEquals("CANCELLED", cancelled.getStatus());
    }
}
