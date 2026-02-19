package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.CustomerRiskScoreDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerRiskScoreService {
    CustomerRiskScoreDTO createRiskScore(CustomerRiskScoreDTO riskScoreDTO);
    Optional<CustomerRiskScoreDTO> getRiskScoreByCustomer(Long companyId, Long customerId);
    Optional<CustomerRiskScoreDTO> getRiskScoreById(Long companyId, Long riskScoreId);
    List<CustomerRiskScoreDTO> getRiskScoresByLevel(Long companyId, String riskLevel);
    List<CustomerRiskScoreDTO> getAtRiskCustomers(Long companyId);
    List<CustomerRiskScoreDTO> getHighDSOCustomers(Long companyId, Integer dsoThreshold);
    List<CustomerRiskScoreDTO> getLatePayerCustomers(Long companyId);
    List<CustomerRiskScoreDTO> getCustomersWithOverdue(Long companyId);
    List<CustomerRiskScoreDTO> getLowCreditScoreCustomers(Long companyId, Integer minCredit);
    List<CustomerRiskScoreDTO> getCreditLimitExceeded(Long companyId);
    List<CustomerRiskScoreDTO> getReviewDueCustomers(Long companyId);
    CustomerRiskScoreDTO updateRiskScore(Long companyId, Long riskScoreId, CustomerRiskScoreDTO riskScoreDTO);
    CustomerRiskScoreDTO calculateAndUpdateRiskScore(Long companyId, Long customerId);
    void deleteRiskScore(Long companyId, Long riskScoreId);
    BigDecimal calculateTotalRiskExposure(Long companyId);
    List<CustomerRiskScoreDTO> getRiskScoresByStatus(Long companyId, String status);
}
