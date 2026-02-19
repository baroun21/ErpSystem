package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.SalesOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SalesOrderLineRepository extends JpaRepository<SalesOrderLine, Long> {

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.salesOrder.id = :orderId ORDER BY sol.createdAt")
    List<SalesOrderLine> findByOrder(@Param("companyId") Long companyId, @Param("orderId") Long orderId);

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.productId = :productId ORDER BY sol.createdAt DESC")
    List<SalesOrderLine> findByProduct(@Param("companyId") Long companyId, @Param("productId") Long productId);

    @Query("SELECT SUM(sol.quantity) FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.productId = :productId")
    BigDecimal sumQuantityByProduct(@Param("companyId") Long companyId, @Param("productId") Long productId);

    @Query("SELECT SUM(sol.lineTotal) FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.salesOrder.id = :orderId")
    BigDecimal sumLineAmountByOrder(@Param("companyId") Long companyId, @Param("orderId") Long orderId);

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.deliveredQuantity < sol.quantity ORDER BY sol.createdAt")
    List<SalesOrderLine> findPendingDeliveries(@Param("companyId") Long companyId);

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.productSku = :sku ORDER BY sol.createdAt DESC")
    List<SalesOrderLine> findByProductSku(@Param("companyId") Long companyId, @Param("sku") String sku);

    @Query("SELECT COUNT(sol) FROM SalesOrderLine sol WHERE sol.companyId = :companyId AND sol.salesOrder.id = :orderId")
    Long countLinesByOrder(@Param("companyId") Long companyId, @Param("orderId") Long orderId);
}
