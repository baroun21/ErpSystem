package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.LeadDTO;
import com.company.erp.erp.entites.sales.Lead;
import com.company.erp.mapper.sales.LeadMapper;
import com.company.userService.HrModule.repositories.LeadRepository;
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
public class LeadServiceTest {

    @Autowired
    private LeadRepository leadRepository;

    private LeadService leadService;
    private LeadMapper leadMapper;
    private Long testCompanyId = 1L;

    @BeforeEach
    void setUp() {
        leadMapper = new LeadMapper() {};
        leadService = new LeadServiceImpl(leadRepository, leadMapper);
    }

    @Test
    void testCreateLead() {
        // Arrange
        LeadDTO leadDTO = LeadDTO.builder()
            .companyId(testCompanyId)
            .firstName("John")
            .lastName("Doe")
            .email("john@example.com")
            .phone("555-1234")
            .companyName("Acme Corp")
            .jobTitle("Manager")
            .source("LinkedIn")
            .status("NEW")
            .estimatedValue(BigDecimal.valueOf(50000))
            .assignedTo(1L)
            .build();

        // Act
        LeadDTO created = leadService.createLead(leadDTO);

        // Assert
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("John", created.getFirstName());
        assertEquals("Doe", created.getLastName());
    }

    @Test
    void testGetLeadsByStatus() {
        // Arrange
        Lead lead1 = Lead.builder()
            .companyId(testCompanyId)
            .firstName("Jane")
            .status("QUALIFIED")
            .createdAt(LocalDateTime.now())
            .build();
        Lead lead2 = Lead.builder()
            .companyId(testCompanyId)
            .firstName("Bob")
            .status("NEW")
            .createdAt(LocalDateTime.now())
            .build();
        leadRepository.saveAll(List.of(lead1, lead2));

        // Act
        List<LeadDTO> qualified = leadService.getLeadsByCompanyAndStatus(testCompanyId, "QUALIFIED");

        // Assert
        assertEquals(1, qualified.size());
        assertEquals("Jane", qualified.get(0).getFirstName());
    }

    @Test
    void testUpdateLead() {
        // Arrange
        Lead lead = Lead.builder()
            .companyId(testCompanyId)
            .firstName("Original")
            .status("NEW")
            .createdAt(LocalDateTime.now())
            .build();
        Lead saved = leadRepository.save(lead);

        LeadDTO updateDTO = LeadDTO.builder()
            .firstName("Updated")
            .status("QUALIFIED")
            .build();

        // Act
        LeadDTO updated = leadService.updateLead(testCompanyId, saved.getId(), updateDTO);

        // Assert
        assertEquals("Updated", updated.getFirstName());
        assertEquals("QUALIFIED", updated.getStatus());
    }

    @Test
    void testDeleteLead() {
        // Arrange
        Lead lead = Lead.builder()
            .companyId(testCompanyId)
            .firstName("ToDelete")
            .createdAt(LocalDateTime.now())
            .build();
        Lead saved = leadRepository.save(lead);

        // Act
        leadService.deleteLead(testCompanyId, saved.getId());

        // Assert
        assertTrue(leadRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void testCountLeadsByStatus() {
        // Arrange
        for (int i = 0; i < 5; i++) {
            Lead lead = Lead.builder()
                .companyId(testCompanyId)
                .firstName("Lead" + i)
                .status("NEW")
                .createdAt(LocalDateTime.now())
                .build();
            leadRepository.save(lead);
        }

        // Act
        Long count = leadService.countLeadsByStatus(testCompanyId, "NEW");

        // Assert
        assertEquals(5, count);
    }
}
