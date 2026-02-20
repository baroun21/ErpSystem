package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesOrderHeaderRepository extends JpaRepository<SalesOrder, Long> {

    @Query("SELECT so FROM SalesOrder so WHERE so.companyId = :companyId " +
           "AND so.orderDate >= :startDate AND so.orderDate < :endDate " +
           "AND so.status != 'CANCELLED' ORDER BY so.orderDate DESC")
    List<SalesOrder> findOrdersByCompanyAndDateRange(@Param("companyId") String companyId,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);
}
