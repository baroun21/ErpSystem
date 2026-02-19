package com.company.erp.procurement.domain.repository;

import com.company.erp.procurement.domain.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("SELECT p FROM PurchaseOrder p WHERE p.companyId = :companyId AND p.poNumber = :number")
    Optional<PurchaseOrder> findByNumberInCompany(@Param("companyId") Long companyId, @Param("number") String number);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.companyId = :companyId ORDER BY p.orderDate DESC")
    List<PurchaseOrder> findAllInCompany(@Param("companyId") Long companyId);
}
