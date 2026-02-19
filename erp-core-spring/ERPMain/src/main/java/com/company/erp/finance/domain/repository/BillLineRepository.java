package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.BillLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillLineRepository extends JpaRepository<BillLine, Long> {

    @Query("SELECT bl FROM BillLine bl WHERE bl.companyId = :companyId AND bl.bill.id = :billId ORDER BY bl.lineNumber")
    List<BillLine> findByBillInCompany(@Param("companyId") Long companyId, @Param("billId") Long billId);

    @Query("SELECT SUM(bl.lineTotal) FROM BillLine bl WHERE bl.companyId = :companyId AND bl.bill.id = :billId")
    java.math.BigDecimal sumLineTotalsForBill(@Param("companyId") Long companyId, @Param("billId") Long billId);

    @Query("SELECT bl FROM BillLine bl WHERE bl.companyId = :companyId AND bl.costCenterId = :costCenterId")
    List<BillLine> findByCostCenterInCompany(@Param("companyId") Long companyId, @Param("costCenterId") Long costCenterId);
}
