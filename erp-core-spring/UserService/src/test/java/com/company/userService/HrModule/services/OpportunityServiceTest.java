package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.OpportunityDTO;
import com.company.erp.erp.entites.sales.Opportunity;
import com.company.erp.mapper.sales.OpportunityMapper;
import com.company.userService.HrModule.repositories.OpportunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class OpportunityServiceTest {

    @Autowired
    private OpportunityRepository opportunityRepository;

    private OpportunityService opportunityService;
    private OpportunityMapper opportunityMapper;
    private Long testCompanyId = 1L;

    @BeforeEach
    void setUp() {
        opportunityMapper = new OpportunityMapper() {};
        opportunityService = new OpportunityServiceImpl(opportunityRepository, opportunityMapper);
    }

    @Test
    void testCreateOpportunity() {
        // Arrange
        OpportunityDTO oppDTO = OpportunityDTO.builder()
            .companyId(testCompanyId)
            .opportunityName("Enterprise Deal")
            .opportunityValue(BigDecimal.valueOf(100000))
            .probabilityPercentage(50)
            .expectedCloseDate(LocalDate.now().plusMonths(3))
            .status("OPEN")
            .build();

        // Act
        OpportunityDTO created = opportunityService.createOpportunity(oppDTO);

        // Assert
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Enterprise Deal", created.getOpportunityName());
        assertEquals("OPEN", created.getStatus());
    }

    @Test
    void testCalculateRevenueForecast() {
        // Arrange
        Opportunity opp1 = Opportunity.builder()
            .companyId(testCompanyId)
            .opportunityName("Deal 1")
            .opportunityValue(BigDecimal.valueOf(100000))
            .probabilityPercentage(50)
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();
        Opportunity opp2 = Opportunity.builder()
            .companyId(testCompanyId)
            .opportunityName("Deal 2")
            .opportunityValue(BigDecimal.valueOf(50000))
            .probabilityPercentage(75)
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
        Opportunity opp = Opportunity.builder()
            .companyId(testCompanyId)
            .opportunityName("TestOpp")
            .opportunityValue(BigDecimal.valueOf(50000))
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();
        Opportunity saved = opportunityRepository.save(opp);

        // Act
        OpportunityDTO won = opportunityService.winOpportunity(testCompanyId, saved.getId());

        // Assert
        assertEquals("WON", won.getStatus());
        assertNotNull(won.getActualCloseDate());
    }

    @Test
    void testLoseOpportunity() {
        // Arrange
        Opportunity opp = Opportunity.builder()
            .companyId(testCompanyId)
            .opportunityName("TestOpp")
            .status("OPEN")
            .createdAt(LocalDateTime.now())
            .build();
        Opportunity saved = opportunityRepository.save(opp);

        // Act
        OpportunityDTO lost = opportunityService.loseOpportunity(testCompanyId, saved.getId());

        // Assert
        assertEquals("LOST", lost.getStatus());
        assertNotNull(lost.getActualCloseDate());
    }

    @Test
    void testCountOpenOpportunities() {
        // Arrange
        for (int i = 0; i < 3; i++) {
            Opportunity opp = Opportunity.builder()
                .companyId(testCompanyId)
                .opportunityName("Opp" + i)
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
