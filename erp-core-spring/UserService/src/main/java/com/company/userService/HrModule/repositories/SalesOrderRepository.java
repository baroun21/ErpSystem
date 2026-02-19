package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.status = :status ORDER BY o.orderDate DESC")
    List<SalesOrder> findByCompanyAndStatus(@Param("companyId") Long companyId, @Param("status") String status);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.customerId = :customerId ORDER BY o.orderDate DESC")
    List<SalesOrder> findByCompanyAndCustomer(@Param("companyId") Long companyId, @Param("customerId") Long customerId);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<SalesOrder> findByDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.paymentStatus = 'OVERDUE' ORDER BY o.payDate DESC")
    List<SalesOrder> findOverdueOrders(@Param("companyId") Long companyId);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.paymentStatus IN ('UNPAID', 'PARTIALLY_PAID') ORDER BY o.paymentDueDate")
    List<SalesOrder> findUnpaidOrders(@Param("companyId") Long companyId);

    @Query("SELECT SUM(o.totalAmount) FROM SalesOrder o WHERE o.companyId = :companyId AND o.customerId = :customerId")
    BigDecimal calculateCustomerLifetimeValue(@Param("companyId") Long companyId, @Param("customerId") Long customerId);

    @Query("SELECT SUM(o.totalAmount) FROM SalesOrder o WHERE o.companyId = :companyId AND o.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenue(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.totalAmount >= :minAmount ORDER BY o.totalAmount DESC")
    List<SalesOrder> findHighValueOrders(@Param("companyId") Long companyId, @Param("minAmount") BigDecimal minAmount);

    @Query("SELECT COUNT(o) FROM SalesOrder o WHERE o.companyId = :companyId AND o.customerId = :customerId")
    Long countCustomerOrders(@Param("companyId") Long companyId, @Param("customerId") Long customerId);

    @Query("SELECT o FROM SalesOrder o WHERE o.companyId = :companyId AND o.referenceOpportunityId = :opportunityId")
    List<SalesOrder> findByOpportunity(@Param("companyId") Long companyId, @Param("opportunityId") Long opportunityId);
}
