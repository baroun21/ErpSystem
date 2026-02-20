package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.CustomerRiskScoreDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerRiskScoreService {
    CustomerRiskScoreDTO createRiskScore(CustomerRiskScoreDTO riskScoreDTO);
    Optional<CustomerRiskScoreDTO> getRiskScoreByCustomer(String companyId, String customerId);
    Optional<CustomerRiskScoreDTO> getRiskScoreById(String companyId, Long riskScoreId);
    List<CustomerRiskScoreDTO> getRiskScoresByLevel(String companyId, String riskLevel);
    List<CustomerRiskScoreDTO> getAtRiskCustomers(String companyId);
    List<CustomerRiskScoreDTO> getHighDSOCustomers(String companyId, BigDecimal dsoThreshold);
    List<CustomerRiskScoreDTO> getLatePayerCustomers(String companyId);
    List<CustomerRiskScoreDTO> getCustomersWithOverdue(String companyId);
    List<CustomerRiskScoreDTO> getLowCreditScoreCustomers(String companyId, Integer minCredit);
    List<CustomerRiskScoreDTO> getCreditLimitExceeded(String companyId);
    List<CustomerRiskScoreDTO> getReviewDueCustomers(String companyId);
    CustomerRiskScoreDTO updateRiskScore(String companyId, Long riskScoreId, CustomerRiskScoreDTO riskScoreDTO);
    CustomerRiskScoreDTO calculateAndUpdateRiskScore(String companyId, String customerId);
    void deleteRiskScore(String companyId, Long riskScoreId);
    BigDecimal calculateTotalRiskExposure(String companyId);
    List<CustomerRiskScoreDTO> getRiskScoresByStatus(String companyId, String status);
}
