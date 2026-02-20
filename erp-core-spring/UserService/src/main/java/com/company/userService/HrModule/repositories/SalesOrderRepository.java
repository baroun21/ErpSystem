package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.status = :status ORDER BY o.orderDate DESC")
    List<SalesOrder> findByCompanyAndStatus(@Param("companyId") String companyId, @Param("status") String status);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.customerId = :customerId ORDER BY o.orderDate DESC")
    List<SalesOrder> findByCompanyAndCustomer(@Param("companyId") String companyId, @Param("customerId") String customerId);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<SalesOrder> findByDateRange(@Param("companyId") String companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.paymentStatus = 'OVERDUE' ORDER BY o.requiredDeliveryDate DESC")
    List<SalesOrder> findOverdueOrders(@Param("companyId") String companyId);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.paymentStatus IN ('UNPAID', 'PARTIALLY_PAID') ORDER BY o.requiredDeliveryDate")
    List<SalesOrder> findUnpaidOrders(@Param("companyId") String companyId);

    @Query("SELECT SUM(o.totalAmount) FROM SalesOrder o WHERE o.companyId = :companyId AND o.customerId = :customerId")
    BigDecimal calculateCustomerLifetimeValue(@Param("companyId") String companyId, @Param("customerId") String customerId);

    @Query("SELECT SUM(o.totalAmount) FROM SalesOrder o WHERE o.companyId = :companyId AND o.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenue(@Param("companyId") String companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.totalAmount >= :minAmount ORDER BY o.totalAmount DESC")
    List<SalesOrder> findHighValueOrders(@Param("companyId") String companyId, @Param("minAmount") BigDecimal minAmount);

    @Query("SELECT COUNT(o) FROM SalesOrder o WHERE o.companyId = :companyId AND o.customerId = :customerId")
    Long countCustomerOrders(@Param("companyId") String companyId, @Param("customerId") String customerId);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.referenceOpportunityId = :opportunityId")
    List<SalesOrder> findByOpportunity(@Param("companyId") String companyId, @Param("opportunityId") Long opportunityId);

    List<SalesOrder> findByCompanyId(String companyId);

    @Query("SELECT o.customerId, SUM(o.totalAmount) FROM SalesOrder o WHERE o.companyId = :companyId GROUP BY o.customerId ORDER BY SUM(o.totalAmount) DESC")
    List<Object[]> findTopCustomersByRevenue(@Param("companyId") String companyId);
}
