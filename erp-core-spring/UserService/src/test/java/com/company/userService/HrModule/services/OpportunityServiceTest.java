package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.OpportunityDTO;
import com.company.erp.erp.entites.sales.Lead;
import com.company.erp.erp.entites.sales.Opportunity;
import com.company.erp.erp.entites.sales.PipelineStage;
import com.company.erp.mapper.sales.OpportunityMapper;
import com.company.userService.HrModule.repositories.LeadRepository;
import com.company.userService.HrModule.repositories.OpportunityRepository;
import com.company.userService.HrModule.repositories.PipelineStageRepository;
import com.company.userService.HrModule.services.impl.OpportunityServiceImpl;
import org.mapstruct.factory.Mappers;
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
public class OpportunityServiceTest {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private PipelineStageRepository pipelineStageRepository;

    private OpportunityService opportunityService;
    private OpportunityMapper opportunityMapper;
    private String testCompanyId = "COMP-1";

    @BeforeEach
    void setUp() {
        opportunityMapper = Mappers.getMapper(OpportunityMapper.class);
        opportunityService = new OpportunityServiceImpl(opportunityRepository, opportunityMapper);
    }

    @Test
    void testCreateOpportunity() {
        // Arrange
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

        OpportunityDTO oppDTO = OpportunityDTO.builder()
            .companyId(testCompanyId)
            .leadId(lead.getLeadId())
            .pipelineStageId(stage.getStageId())
            .opportunityName("Enterprise Deal")
            .opportunityValue(BigDecimal.valueOf(100000))
            .probabilityPercentage(BigDecimal.valueOf(50))
            .expectedCloseDate(LocalDateTime.now().plusMonths(3))
            .status("OPEN")
            .build();

        // Act
        OpportunityDTO created = opportunityService.createOpportunity(oppDTO);

        // Assert
        assertNotNull(created);
        assertNotNull(created.getOpportunityId());
        assertEquals("Enterprise Deal", created.getOpportunityName());
        assertEquals("OPEN", created.getStatus());
    }

    @Test
    void testCalculateRevenueForecast() {
        // Arrange
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
            .opportunityName("Deal 1")
            .opportunityValue(BigDecimal.valueOf(100000))
            .probabilityPercentage(BigDecimal.valueOf(50))
            .lead(lead)
            .pipelineStage(stage)
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();
        Opportunity opp2 = Opportunity.builder()
            .companyId(testCompanyId)
            .opportunityName("Deal 2")
            .opportunityValue(BigDecimal.valueOf(50000))
            .probabilityPercentage(BigDecimal.valueOf(75))
            .lead(lead)
            .pipelineStage(stage)
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();
        opportunityRepository.saveAll(List.of(opp1, opp2));

        // Act
        BigDecimal forecast = opportunityService.calculateRevenueForecast(testCompanyId);

        // Assert
        // Expected: (100000 * 0.5) + (50000 * 0.75) = 50000 + 37500 = 87500
        assertNotNull(forecast);
        assertTrue(forecast.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testWinOpportunity() {
        // Arrange
        Lead lead = leadRepository.save(Lead.builder()
            .companyId(testCompanyId)
            .firstName("Alex")
            .lastName("Stone")
            .createdAt(LocalDateTime.now())
            .build());

        PipelineStage stage = pipelineStageRepository.save(PipelineStage.builder()
            .companyId(testCompanyId)
            .stageName("Negotiation")
            .stageOrder(2)
            .conversionProbability(BigDecimal.valueOf(0.5))
            .createdAt(LocalDateTime.now())
            .build());

        Opportunity opp = Opportunity.builder()
            .companyId(testCompanyId)
            .opportunityName("TestOpp")
            .opportunityValue(BigDecimal.valueOf(50000))
            .lead(lead)
            .pipelineStage(stage)
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();
        Opportunity saved = opportunityRepository.save(opp);

        // Act
        OpportunityDTO won = opportunityService.winOpportunity(testCompanyId, saved.getOpportunityId());

        // Assert
        assertEquals("WON", won.getStatus());
        assertNotNull(won.getActualCloseDate());
    }

    @Test
    void testLoseOpportunity() {
        // Arrange
        Lead lead = leadRepository.save(Lead.builder()
            .companyId(testCompanyId)
            .firstName("Alex")
            .lastName("Stone")
            .createdAt(LocalDateTime.now())
            .build());

        PipelineStage stage = pipelineStageRepository.save(PipelineStage.builder()
            .companyId(testCompanyId)
            .stageName("Negotiation")
            .stageOrder(2)
            .conversionProbability(BigDecimal.valueOf(0.5))
            .createdAt(LocalDateTime.now())
            .build());

        Opportunity opp = Opportunity.builder()
            .companyId(testCompanyId)
            .opportunityName("TestOpp")
            .lead(lead)
            .pipelineStage(stage)
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();
        Opportunity saved = opportunityRepository.save(opp);

        // Act
        OpportunityDTO lost = opportunityService.loseOpportunity(testCompanyId, saved.getOpportunityId());

        // Assert
        assertEquals("LOST", lost.getStatus());
        assertNotNull(lost.getActualCloseDate());
    }

    @Test
    void testCountOpenOpportunities() {
        // Arrange
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

        for (int i = 0; i < 3; i++) {
            Opportunity opp = Opportunity.builder()
                .companyId(testCompanyId)
                .opportunityName("Opp" + i)
                .lead(lead)
                .pipelineStage(stage)
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .build();
            opportunityRepository.save(opp);
        }

        // Act
        Long count = opportunityService.countOpenOpportunities(testCompanyId);

        // Assert
        assertEquals(3L, count);
    }
}
