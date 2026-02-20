package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.CustomerRiskScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRiskScoreRepository extends JpaRepository<CustomerRiskScore, Long> {

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.customerId = :customerId")
    CustomerRiskScore findByCompanyAndCustomer(@Param("companyId") String companyId, @Param("customerId") String customerId);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.riskLevel = :riskLevel ORDER BY c.overallRiskScore DESC")
    List<CustomerRiskScore> findByCompanyAndRiskLevel(@Param("companyId") String companyId, @Param("riskLevel") String riskLevel);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.status = :status ORDER BY c.overallRiskScore DESC")
    List<CustomerRiskScore> findByCompanyAndStatus(@Param("companyId") String companyId, @Param("status") String status);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.riskLevel IN ('HIGH', 'CRITICAL') ORDER BY c.overallRiskScore DESC")
    List<CustomerRiskScore> findAtRiskCustomers(@Param("companyId") String companyId);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.daysSalesOutstanding > :dsoThreshold ORDER BY c.daysSalesOutstanding DESC")
    List<CustomerRiskScore> findHighDSOCustomers(@Param("companyId") String companyId, @Param("dsoThreshold") BigDecimal dsoThreshold);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.latePaymentCount > 0 ORDER BY c.latePaymentCount DESC")
    List<CustomerRiskScore> findLatePayerCustomers(@Param("companyId") String companyId);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.totalOverdueAmount > 0 ORDER BY c.totalOverdueAmount DESC")
    List<CustomerRiskScore> findCustomersWithOverdue(@Param("companyId") String companyId);

    @Query("SELECT SUM(c.totalOverdueAmount) FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.riskLevel IN ('HIGH', 'CRITICAL')")
    BigDecimal calculateTotalRiskExposure(@Param("companyId") String companyId);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.creditScore < :minCredit ORDER BY c.creditScore")
    List<CustomerRiskScore> findLowCreditScoreCustomers(@Param("companyId") String companyId, @Param("minCredit") Integer minCredit);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.nextReviewDate <= CURRENT_DATE")
    List<CustomerRiskScore> findReviewDueCustomers(@Param("companyId") String companyId);

    @Query("SELECT c FROM CustomerRiskScore c WHERE c.companyId = :companyId AND c.currentExposure > c.creditLimit ORDER BY c.currentExposure DESC")
    List<CustomerRiskScore> findCreditLimitExceeded(@Param("companyId") String companyId);
}
