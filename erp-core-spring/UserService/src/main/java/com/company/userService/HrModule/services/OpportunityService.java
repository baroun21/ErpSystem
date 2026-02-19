package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.OpportunityDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OpportunityService {
    OpportunityDTO createOpportunity(OpportunityDTO opportunityDTO);
    Optional<OpportunityDTO> getOpportunityById(Long companyId, Long opportunityId);
    List<OpportunityDTO> getOpportunitiesByStatus(Long companyId, String status);
    List<OpportunityDTO> getOpportunitiesByStage(Long companyId, Long stageId);
    List<OpportunityDTO> getOpportunitiesByLead(Long companyId, Long leadId);
    List<OpportunityDTO> getOpportunitiesByCloseDateRange(Long companyId, LocalDate startDate, LocalDate endDate);
    List<OpportunityDTO> getHighProbabilityOpportunities(Long companyId, Integer minProbability);
    OpportunityDTO updateOpportunity(Long companyId, Long opportunityId, OpportunityDTO opportunityDTO);
    OpportunityDTO winOpportunity(Long companyId, Long opportunityId);
    OpportunityDTO loseOpportunity(Long companyId, Long opportunityId);
    void deleteOpportunity(Long companyId, Long opportunityId);
    BigDecimal calculateRevenueForecast(Long companyId);
    Long countOpenOpportunities(Long companyId);
    List<OpportunityDTO> getForecastingOpportunities(Long companyId);
}
