package com.company.userService.HrModule.services.impl;

import com.company.erp.erp.Dtos.sales.LeadDTO;
import com.company.erp.erp.entites.sales.Lead;
import com.company.erp.mapper.sales.LeadMapper;
import com.company.userService.HrModule.repositories.LeadRepository;
import com.company.userService.HrModule.services.LeadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;

    @Override
    public LeadDTO createLead(LeadDTO leadDTO) {
        log.info("Creating lead for company: {}", leadDTO.getCompanyId());
        Lead lead = leadMapper.toEntity(leadDTO);
        lead.setCreatedAt(LocalDateTime.now());
        Lead saved = leadRepository.save(lead);
        return leadMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeadDTO> getLeadById(String companyId, Long leadId) {
        return leadRepository.findById(leadId)
            .filter(lead -> lead.getCompanyId().equals(companyId))
            .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsByStatus(String companyId, String status) {
        log.info("Fetching leads for company: {} with status: {}", companyId, status);
        return leadRepository.findByCompanyAndStatus(companyId, status)
            .stream()
            .map(leadMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsBySource(String companyId, String source) {
        log.info("Fetching leads for company: {} from source: {}", companyId, source);
        return leadRepository.findByCompanyAndSource(companyId, source)
            .stream()
            .map(leadMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsByAssignedTo(String companyId, String assignedTo) {
        log.info("Fetching assigned leads for sales rep: {}", assignedTo);
        return leadRepository.findByCompanyAndAssignedTo(companyId, assignedTo)
            .stream()
            .map(leadMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getHighValueLeads(String companyId, java.math.BigDecimal minValue) {
        log.info("Fetching high value leads for company: {} with min value: {}", companyId, minValue);
        return leadRepository.findHighValueLeads(companyId, minValue)
            .stream()
            .map(leadMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsByDateRange(String companyId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching leads for company: {} between {} and {}", companyId, startDate, endDate);
        return leadRepository.findByCompanyAndDateRange(companyId, startDate, endDate)
            .stream()
            .map(leadMapper::toDTO)
            .toList();
    }

    @Override
    public LeadDTO updateLead(String companyId, Long leadId, LeadDTO leadDTO) {
        log.info("Updating lead: {} for company: {}", leadId, companyId);
        Lead lead = leadRepository.findById(leadId)
            .filter(l -> l.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Lead not found or access denied"));
        
        leadMapper.updateEntityFromDTO(leadDTO, lead);
        lead.setUpdatedAt(LocalDateTime.now());
        Lead updated = leadRepository.save(lead);
        return leadMapper.toDTO(updated);
    }

    @Override
    public void deleteLead(String companyId, Long leadId) {
        log.info("Deleting lead: {} for company: {}", leadId, companyId);
        Lead lead = leadRepository.findById(leadId)
            .filter(l -> l.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Lead not found or access denied"));
        leadRepository.delete(lead);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLeadsByStatus(String companyId, String status) {
        return leadRepository.countByStatus(companyId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> searchLeadsByEmail(String companyId, String email) {
        return leadRepository.findByEmailContaining(companyId, email)
            .stream()
            .map(leadMapper::toDTO)
            .toList();
    }
}
