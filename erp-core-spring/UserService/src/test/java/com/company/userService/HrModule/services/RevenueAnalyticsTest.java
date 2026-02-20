package com.company.userService.HrModule.services;

import com.company.erp.erp.entites.sales.Lead;
import com.company.erp.erp.entites.sales.Opportunity;
import com.company.erp.erp.entites.sales.PipelineStage;
import com.company.userService.HrModule.repositories.LeadRepository;
import com.company.userService.HrModule.repositories.OpportunityRepository;
import com.company.userService.HrModule.repositories.PipelineStageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RevenueAnalyticsTest {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private PipelineStageRepository pipelineStageRepository;

    @Autowired
    private LeadRepository leadRepository;

    private RevenueAnalyticsService revenueAnalyticsService;
    private String testCompanyId = "COMP-1";

    @BeforeEach
    void setUp() {
        revenueAnalyticsService = new RevenueAnalyticsService(opportunityRepository, pipelineStageRepository);
    }

    @Test
    void testCalculateTotalRevenueForecast() {
        Lead lead = leadRepository.save(Lead.builder()
            .companyId(testCompanyId)
            .firstName("Alex")
            .lastName("Stone")
            .createdAt(LocalDateTime.now())
            .build());

        PipelineStage stage = pipelineStageRepository.save(PipelineStage.builder()
            .companyId(testCompanyId)
            .stageName("Qualification")
            .stageOrder(1)
            .conversionProbability(BigDecimal.valueOf(0.25))
            .createdAt(LocalDateTime.now())
            .build());

        Opportunity opp1 = Opportunity.builder()
            .companyId(testCompanyId)
            .lead(lead)
            .pipelineStage(stage)
            .opportunityName("Deal 1")
            .opportunityValue(BigDecimal.valueOf(100000))
            .probabilityPercentage(BigDecimal.valueOf(50))
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();

        Opportunity opp2 = Opportunity.builder()
            .companyId(testCompanyId)
            .lead(lead)
            .pipelineStage(stage)
            .opportunityName("Deal 2")
            .opportunityValue(BigDecimal.valueOf(50000))
            .probabilityPercentage(BigDecimal.valueOf(75))
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();

        opportunityRepository.saveAll(List.of(opp1, opp2));

        BigDecimal forecast = revenueAnalyticsService.calculateTotalRevenueForecast(testCompanyId);
        assertNotNull(forecast);
        assertTrue(forecast.compareTo(BigDecimal.ZERO) > 0);
    }
}
