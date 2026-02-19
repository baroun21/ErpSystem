package com.company.userService.HrModule.services.impl;

import com.company.erp.erp.Dtos.sales.CustomerRiskScoreDTO;
import com.company.erp.erp.entites.sales.CustomerRiskScore;
import com.company.erp.mapper.sales.CustomerRiskScoreMapper;
import com.company.userService.HrModule.repositories.CustomerRiskScoreRepository;
import com.company.userService.HrModule.services.CustomerRiskScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerRiskScoreServiceImpl implements CustomerRiskScoreService {

    private final CustomerRiskScoreRepository customerRiskScoreRepository;
    private final CustomerRiskScoreMapper customerRiskScoreMapper;

    @Override
    public CustomerRiskScoreDTO createRiskScore(CustomerRiskScoreDTO riskScoreDTO) {
        log.info("Creating risk score for customer: {} in company: {}", riskScoreDTO.getCustomerId(), riskScoreDTO.getCompanyId());
        CustomerRiskScore riskScore = customerRiskScoreMapper.toEntity(riskScoreDTO);
        riskScore.setCreatedAt(LocalDateTime.now());
        if (riskScore.getStatus() == null) {
            riskScore.setStatus("ACTIVE");
        }
        if (riskScore.getLastReviewDate() == null) {
            riskScore.setLastReviewDate(LocalDateTime.now());
        }
        CustomerRiskScore saved = customerRiskScoreRepository.save(riskScore);
        return customerRiskScoreMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerRiskScoreDTO> getRiskScoreByCustomer(Long companyId, Long customerId) {
        return Optional.ofNullable(customerRiskScoreRepository.findByCompanyAndCustomer(companyId, customerId))
            .map(customerRiskScoreMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerRiskScoreDTO> getRiskScoreById(Long companyId, Long riskScoreId) {
        return customerRiskScoreRepository.findById(riskScoreId)
            .filter(rs -> rs.getCompanyId().equals(companyId))
            .map(customerRiskScoreMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getRiskScoresByLevel(Long companyId, String riskLevel) {
        log.info("Fetching risk scores for company: {} with level: {}", companyId, riskLevel);
        return customerRiskScoreRepository.findByCompanyAndRiskLevel(companyId, riskLevel)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getAtRiskCustomers(Long companyId) {
        log.info("Fetching at-risk customers for company: {}", companyId);
        return customerRiskScoreRepository.findAtRiskCustomers(companyId)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getHighDSOCustomers(Long companyId, Integer dsoThreshold) {
        log.info("Fetching customers with high DSO (> {}) for company: {}", dsoThreshold, companyId);
        return customerRiskScoreRepository.findHighDSOCustomers(companyId, dsoThreshold)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getLatePayerCustomers(Long companyId) {
        log.info("Fetching late pay customers for company: {}", companyId);
        return customerRiskScoreRepository.findLatePayerCustomers(companyId)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getCustomersWithOverdue(Long companyId) {
        log.info("Fetching customers with overdue amounts for company: {}", companyId);
        return customerRiskScoreRepository.findCustomersWithOverdue(companyId)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getLowCreditScoreCustomers(Long companyId, Integer minCredit) {
        log.info("Fetching customers with credit score < {} for company: {}", minCredit, companyId);
        return customerRiskScoreRepository.findLowCreditScoreCustomers(companyId, minCredit)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getCreditLimitExceeded(Long companyId) {
        log.info("Fetching customers exceeding credit limit for company: {}", companyId);
        return customerRiskScoreRepository.findCreditLimitExceeded(companyId)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getReviewDueCustomers(Long companyId) {
        log.info("Fetching customers due for review for company: {}", companyId);
        return customerRiskScoreRepository.findReviewDueCustomers(companyId)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    @Override
    public CustomerRiskScoreDTO updateRiskScore(Long companyId, Long riskScoreId, CustomerRiskScoreDTO riskScoreDTO) {
        log.info("Updating risk score: {} for company: {}", riskScoreId, companyId);
        CustomerRiskScore riskScore = customerRiskScoreRepository.findById(riskScoreId)
            .filter(rs -> rs.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Risk score not found or access denied"));
        
        customerRiskScoreMapper.updateEntityFromDTO(riskScoreDTO, riskScore);
        riskScore.setUpdatedAt(LocalDateTime.now());
        CustomerRiskScore updated = customerRiskScoreRepository.save(riskScore);
        return customerRiskScoreMapper.toDTO(updated);
    }

    @Override
    public CustomerRiskScoreDTO calculateAndUpdateRiskScore(Long companyId, Long customerId) {
        log.info("Calculating and updating risk score for customer: {}", customerId);
        CustomerRiskScore riskScore = customerRiskScoreRepository.findByCompanyAndCustomer(companyId, customerId);
        
        if (riskScore == null) {
            throw new RuntimeException("Risk score not found for customer");
        }
        
        // Calculate overall risk score based on components
        Integer overallRiskScore = calculateOverallRiskScore(riskScore);
        riskScore.setOverallRiskScore(BigDecimal.valueOf(overallRiskScore));
        
        // Determine risk level based on overall score
        String riskLevel = determineRiskLevel(overallRiskScore);
        riskScore.setRiskLevel(riskLevel);
        
        riskScore.setLastReviewDate(LocalDateTime.now());
        riskScore.setNextReviewDate(LocalDateTime.now().plusDays(90));
        riskScore.setUpdatedAt(LocalDateTime.now());
        
        CustomerRiskScore updated = customerRiskScoreRepository.save(riskScore);
        return customerRiskScoreMapper.toDTO(updated);
    }

    @Override
    public void deleteRiskScore(Long companyId, Long riskScoreId) {
        log.info("Deleting risk score: {} for company: {}", riskScoreId, companyId);
        CustomerRiskScore riskScore = customerRiskScoreRepository.findById(riskScoreId)
            .filter(rs -> rs.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Risk score not found or access denied"));
        customerRiskScoreRepository.delete(riskScore);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalRiskExposure(Long companyId) {
        log.info("Calculating total risk exposure for company: {}", companyId);
        return customerRiskScoreRepository.calculateTotalRiskExposure(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRiskScoreDTO> getRiskScoresByStatus(Long companyId, String status) {
        log.info("Fetching risk scores for company: {} with status: {}", companyId, status);
        return customerRiskScoreRepository.findByCompanyAndStatus(companyId, status)
            .stream()
            .map(customerRiskScoreMapper::toDTO)
            .toList();
    }

    private Integer calculateOverallRiskScore(CustomerRiskScore riskScore) {
        // Weighted calculation: Credit(25%) + PaymentHistory(25%) + DSO(25%) + LatePayments(25%)
        Integer creditComponent = (riskScore.getCreditScore() != null) ? riskScore.getCreditScore() : 0;
        Integer historyComponent = (riskScore.getPaymentHistoryScore() != null) ? riskScore.getPaymentHistoryScore() : 0;
        Integer dsoComponent = calculateDSOComponent(riskScore.getDaysSalesOutstanding());
        Integer lateComponent = calculateLatePaymentComponent(riskScore.getLatePaymentCount());
        
        return (creditComponent + historyComponent + dsoComponent + lateComponent) / 4;
    }

    private Integer calculateDSOComponent(BigDecimal dso) {
        if (dso == null || dso.intValue() <= 30) return 25;
        if (dso.intValue() <= 60) return 50;
        if (dso.intValue() <= 90) return 75;
        return 100;
    }

    private Integer calculateLatePaymentComponent(Integer lateCount) {
        if (lateCount == null || lateCount == 0) return 25;
        if (lateCount <= 2) return 50;
        if (lateCount <= 5) return 75;
        return 100;
    }

    private String determineRiskLevel(Integer overallScore) {
        if (overallScore == null) overallScore = 0;
        if (overallScore <= 25) return "LOW";
        if (overallScore <= 50) return "MEDIUM";
        if (overallScore <= 75) return "HIGH";
        return "CRITICAL";
    }
}
