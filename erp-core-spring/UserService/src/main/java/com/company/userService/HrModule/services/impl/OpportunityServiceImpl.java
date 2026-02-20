package com.company.userService.HrModule.services.impl;

import com.company.erp.erp.Dtos.sales.OpportunityDTO;
import com.company.erp.erp.entites.sales.Opportunity;
import com.company.erp.mapper.sales.OpportunityMapper;
import com.company.userService.HrModule.repositories.OpportunityRepository;
import com.company.userService.HrModule.services.OpportunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final OpportunityMapper opportunityMapper;

    @Override
    public OpportunityDTO createOpportunity(OpportunityDTO opportunityDTO) {
        log.info("Creating opportunity for company: {}", opportunityDTO.getCompanyId());
        Opportunity opportunity = opportunityMapper.toEntity(opportunityDTO);
        opportunity.setCreatedAt(LocalDateTime.now());
        if (opportunityDTO.getStatus() == null) {
            opportunity.setStatus("OPEN");
        }
        Opportunity saved = opportunityRepository.save(opportunity);
        return opportunityMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OpportunityDTO> getOpportunityById(String companyId, Long opportunityId) {
        return opportunityRepository.findById(opportunityId)
            .filter(opp -> opp.getCompanyId().equals(companyId))
            .map(opportunityMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> getOpportunitiesByStatus(String companyId, String status) {
        log.info("Fetching opportunities for company: {} with status: {}", companyId, status);
        return opportunityRepository.findByCompanyAndStatus(companyId, status)
            .stream()
            .map(opportunityMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> getOpportunitiesByStage(String companyId, Long stageId) {
        log.info("Fetching opportunities for company: {} in stage: {}", companyId, stageId);
        return opportunityRepository.findByCompanyAndStage(companyId, stageId)
            .stream()
            .map(opportunityMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> getOpportunitiesByLead(String companyId, Long leadId) {
        return opportunityRepository.findByCompanyAndLead(companyId, leadId)
            .stream()
            .map(opportunityMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> getOpportunitiesByCloseDateRange(String companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return opportunityRepository.findByCloseDateRange(companyId, startDate, endDate)
            .stream()
            .map(opportunityMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> getHighProbabilityOpportunities(String companyId, Integer minProbability) {
        return opportunityRepository.findHighProbabilityOpportunities(companyId, minProbability)
            .stream()
            .map(opportunityMapper::toDTO)
            .toList();
    }

    @Override
    public OpportunityDTO updateOpportunity(String companyId, Long opportunityId, OpportunityDTO opportunityDTO) {
        log.info("Updating opportunity: {} for company: {}", opportunityId, companyId);
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
            .filter(opp -> opp.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Opportunity not found or access denied"));
        
        opportunityMapper.updateEntityFromDTO(opportunityDTO, opportunity);
        opportunity.setUpdatedAt(LocalDateTime.now());
        Opportunity updated = opportunityRepository.save(opportunity);
        return opportunityMapper.toDTO(updated);
    }

    @Override
    public OpportunityDTO winOpportunity(String companyId, Long opportunityId) {
        log.info("Marking opportunity as won: {}", opportunityId);
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
            .filter(opp -> opp.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Opportunity not found or access denied"));
        
        opportunity.setStatus("WON");
        opportunity.setActualCloseDate(LocalDateTime.now());
        opportunity.setUpdatedAt(LocalDateTime.now());
        Opportunity updated = opportunityRepository.save(opportunity);
        return opportunityMapper.toDTO(updated);
    }

    @Override
    public OpportunityDTO loseOpportunity(String companyId, Long opportunityId) {
        log.info("Marking opportunity as lost: {}", opportunityId);
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
            .filter(opp -> opp.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Opportunity not found or access denied"));
        
        opportunity.setStatus("LOST");
        opportunity.setActualCloseDate(LocalDateTime.now());
        opportunity.setUpdatedAt(LocalDateTime.now());
        Opportunity updated = opportunityRepository.save(opportunity);
        return opportunityMapper.toDTO(updated);
    }

    @Override
    public void deleteOpportunity(String companyId, Long opportunityId) {
        log.info("Deleting opportunity: {} for company: {}", opportunityId, companyId);
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
            .filter(opp -> opp.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Opportunity not found or access denied"));
        opportunityRepository.delete(opportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateRevenueForecast(String companyId) {
        log.info("Calculating revenue forecast for company: {}", companyId);
        return opportunityRepository.calculateRevenueForecast(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countOpenOpportunities(String companyId) {
        return opportunityRepository.countOpenOpportunities(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> getForecastingOpportunities(String companyId) {
        return opportunityRepository.findForecasting(companyId)
            .stream()
            .map(opportunityMapper::toDTO)
            .toList();
    }
}
