package com.company.userService.HrModule.services;

import com.company.erp.erp.entites.sales.Opportunity;
import com.company.userService.HrModule.repositories.OpportunityRepository;
import com.company.userService.HrModule.repositories.PipelineStageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RevenueAnalyticsService {

    private final OpportunityRepository opportunityRepository;
    private final PipelineStageRepository pipelineStageRepository;

    public BigDecimal calculateTotalRevenueForecast(String companyId) {
        BigDecimal forecast = opportunityRepository.calculateRevenueForecast(companyId);
        return forecast != null ? forecast : BigDecimal.ZERO;
    }

    public Map<String, BigDecimal> getForecastByStage(String companyId) {
        List<Opportunity> opportunities = opportunityRepository.findForecasting(companyId);
        return opportunities.stream()
            .filter(opportunity -> opportunity.getPipelineStage() != null)
            .collect(Collectors.groupingBy(
                opportunity -> opportunity.getPipelineStage().getStageName(),
                Collectors.reducing(BigDecimal.ZERO, this::calculateWeightedValue, BigDecimal::add)
            ));
    }

    public Map<String, Object> getDetailedForecastByStage(String companyId) {
        List<Opportunity> opportunities = opportunityRepository.findForecasting(companyId);
        Map<String, Object> result = new LinkedHashMap<>();
        opportunities.stream()
            .filter(opportunity -> opportunity.getPipelineStage() != null)
            .collect(Collectors.groupingBy(opportunity -> opportunity.getPipelineStage().getStageName()))
            .forEach((stageName, stageOpportunities) -> {
                BigDecimal revenue = stageOpportunities.stream()
                    .map(this::calculateWeightedValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> details = new LinkedHashMap<>();
                details.put("count", stageOpportunities.size());
                details.put("revenue", revenue);
                result.put(stageName, details);
            });
        return result;
    }

    public Map<String, BigDecimal> getForecastByMonth(String companyId) {
        List<Opportunity> opportunities = opportunityRepository.findForecasting(companyId);
        return opportunities.stream()
            .filter(opportunity -> opportunity.getExpectedCloseDate() != null)
            .collect(Collectors.groupingBy(
                opportunity -> YearMonth.from(opportunity.getExpectedCloseDate()).toString(),
                Collectors.reducing(BigDecimal.ZERO, this::calculateWeightedValue, BigDecimal::add)
            ));
    }

    public BigDecimal getBestCaseRevenue(String companyId) {
        return opportunityRepository.findForecasting(companyId).stream()
            .map(opportunity -> defaultZero(opportunity.getOpportunityValue()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getWorstCaseRevenue(String companyId) {
        return BigDecimal.ZERO;
    }

    public BigDecimal getMostLikelyRevenue(String companyId) {
        return calculateTotalRevenueForecast(companyId);
    }

    public BigDecimal getAverageDealSize(String companyId) {
        List<Opportunity> opportunities = opportunityRepository.findForecasting(companyId);
        if (opportunities.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = opportunities.stream()
            .map(opportunity -> defaultZero(opportunity.getOpportunityValue()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(opportunities.size()), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getForecastConcentration(String companyId) {
        List<Opportunity> opportunities = opportunityRepository.findForecasting(companyId);
        if (opportunities.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = opportunities.stream()
            .map(this::calculateWeightedValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal topFive = opportunities.stream()
            .sorted(Comparator.comparing((Opportunity opp) -> defaultZero(opp.getOpportunityValue())).reversed())
            .limit(5)
            .map(this::calculateWeightedValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return topFive.multiply(BigDecimal.valueOf(100)).divide(total, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateWeightedValue(Opportunity opportunity) {
        BigDecimal value = defaultZero(opportunity.getOpportunityValue());
        BigDecimal probability = defaultZero(opportunity.getProbabilityPercentage());
        return value.multiply(probability).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
