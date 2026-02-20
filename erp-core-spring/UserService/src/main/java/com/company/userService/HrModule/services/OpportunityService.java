package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.OpportunityDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OpportunityService {
    OpportunityDTO createOpportunity(OpportunityDTO opportunityDTO);
    Optional<OpportunityDTO> getOpportunityById(String companyId, Long opportunityId);
    List<OpportunityDTO> getOpportunitiesByStatus(String companyId, String status);
    List<OpportunityDTO> getOpportunitiesByStage(String companyId, Long stageId);
    List<OpportunityDTO> getOpportunitiesByLead(String companyId, Long leadId);
    List<OpportunityDTO> getOpportunitiesByCloseDateRange(String companyId, LocalDateTime startDate, LocalDateTime endDate);
    List<OpportunityDTO> getHighProbabilityOpportunities(String companyId, Integer minProbability);
    List<OpportunityDTO> getForecastingOpportunities(String companyId);
    OpportunityDTO updateOpportunity(String companyId, Long opportunityId, OpportunityDTO opportunityDTO);
    OpportunityDTO winOpportunity(String companyId, Long opportunityId);
    OpportunityDTO loseOpportunity(String companyId, Long opportunityId);
    void deleteOpportunity(String companyId, Long opportunityId);
    BigDecimal calculateRevenueForecast(String companyId);
    Long countOpenOpportunities(String companyId);
}
