package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.SalesOrderDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesOrderService {
    SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO);
    Optional<SalesOrderDTO> getSalesOrderById(Long companyId, Long orderId);
    List<SalesOrderDTO> getSalesOrdersByStatus(Long companyId, String status);
    List<SalesOrderDTO> getSalesOrdersByCustomer(Long companyId, Long customerId);
    List<SalesOrderDTO> getSalesOrdersByDateRange(Long companyId, LocalDate startDate, LocalDate endDate);
    List<SalesOrderDTO> getOverdueOrders(Long companyId);
    List<SalesOrderDTO> getUnpaidOrders(Long companyId);
    List<SalesOrderDTO> getHighValueOrders(Long companyId, BigDecimal minAmount);
    SalesOrderDTO updateSalesOrder(Long companyId, Long orderId, SalesOrderDTO salesOrderDTO);
    SalesOrderDTO confirmOrder(Long companyId, Long orderId);
    SalesOrderDTO shipOrder(Long companyId, Long orderId);
    SalesOrderDTO deliverOrder(Long companyId, Long orderId);
    SalesOrderDTO cancelOrder(Long companyId, Long orderId);
    void deleteSalesOrder(Long companyId, Long orderId);
    BigDecimal calculateCustomerLifetimeValue(Long companyId, Long customerId);
    BigDecimal calculateRevenue(Long companyId, LocalDate startDate, LocalDate endDate);
    Long countCustomerOrders(Long companyId, Long customerId);
    List<SalesOrderDTO> getOrdersByOpportunity(Long companyId, Long opportunityId);
}
