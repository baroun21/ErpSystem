package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.SalesOrderDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SalesOrderService {
    SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO);
    Optional<SalesOrderDTO> getSalesOrderById(String companyId, Long orderId);
    List<SalesOrderDTO> getSalesOrdersByStatus(String companyId, String status);
    List<SalesOrderDTO> getSalesOrdersByCustomer(String companyId, String customerId);
    List<SalesOrderDTO> getSalesOrdersByDateRange(String companyId, LocalDateTime startDate, LocalDateTime endDate);
    List<SalesOrderDTO> getOverdueOrders(String companyId);
    List<SalesOrderDTO> getUnpaidOrders(String companyId);
    List<SalesOrderDTO> getHighValueOrders(String companyId, BigDecimal minAmount);
    SalesOrderDTO updateSalesOrder(String companyId, Long orderId, SalesOrderDTO salesOrderDTO);
    SalesOrderDTO confirmOrder(String companyId, Long orderId);
    SalesOrderDTO shipOrder(String companyId, Long orderId);
    SalesOrderDTO deliverOrder(String companyId, Long orderId);
    SalesOrderDTO cancelOrder(String companyId, Long orderId);
    void deleteSalesOrder(String companyId, Long orderId);
    BigDecimal calculateCustomerLifetimeValue(String companyId, String customerId);
    BigDecimal calculateRevenue(String companyId, LocalDateTime startDate, LocalDateTime endDate);
    Long countCustomerOrders(String companyId, String customerId);
    List<SalesOrderDTO> getOrdersByOpportunity(String companyId, Long opportunityId);
}
