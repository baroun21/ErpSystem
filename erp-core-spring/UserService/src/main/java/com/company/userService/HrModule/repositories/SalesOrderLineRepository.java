package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.SalesOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesOrderLineRepository extends JpaRepository<SalesOrderLine, Long> {

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.salesOrder.salesOrderId = :orderId ORDER BY sol.lineNumber")
    List<SalesOrderLine> findByOrder(@Param("companyId") String companyId, @Param("orderId") Long orderId);

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.productId = :productId ORDER BY sol.lineNumber DESC")
    List<SalesOrderLine> findByProduct(@Param("companyId") String companyId, @Param("productId") String productId);

    @Query("SELECT SUM(sol.quantity) FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.productId = :productId")
    BigDecimal sumQuantityByProduct(@Param("companyId") String companyId, @Param("productId") String productId);

    @Query("SELECT SUM(sol.lineTotal) FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.salesOrder.salesOrderId = :orderId")
    BigDecimal sumLineAmountByOrder(@Param("companyId") String companyId, @Param("orderId") Long orderId);

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.deliveredQuantity < sol.quantity ORDER BY sol.lineNumber")
    List<SalesOrderLine> findPendingDeliveries(@Param("companyId") String companyId);

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.productSku = :sku ORDER BY sol.lineNumber DESC")
    List<SalesOrderLine> findByProductSku(@Param("companyId") String companyId, @Param("sku") String sku);

    @Query("SELECT COUNT(sol) FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.salesOrder.salesOrderId = :orderId")
    Long countLinesByOrder(@Param("companyId") String companyId, @Param("orderId") Long orderId);

    @Query("SELECT sol.productId, sol.productName, SUM(sol.quantity), SUM(sol.lineTotal) " +
           "FROM SalesOrderLine sol WHERE sol.companyId = :companyId " +
           "GROUP BY sol.productId, sol.productName ORDER BY SUM(sol.lineTotal) DESC")
    List<Object[]> findTopProducts(@Param("companyId") String companyId, Pageable pageable);

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId " +
           "AND sol.salesOrder.orderDate >= :startDate AND sol.salesOrder.orderDate < :endDate " +
           "AND sol.salesOrder.status != 'CANCELLED' ORDER BY sol.salesOrder.orderDate DESC")
    List<SalesOrderLine> findActiveLinesByCompanyAndDateRange(@Param("companyId") String companyId, 
                                                              @Param("startDate") LocalDate startDate,
                                                              @Param("endDate") LocalDate endDate);
}
