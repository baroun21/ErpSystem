package com.company.userService.HrModule.services.impl;

import com.company.erp.erp.Dtos.sales.QuotationDTO;
import com.company.erp.erp.entites.sales.Quotation;
import com.company.erp.mapper.sales.QuotationMapper;
import com.company.userService.HrModule.repositories.QuotationRepository;
import com.company.userService.HrModule.services.QuotationService;
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
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationMapper quotationMapper;

    @Override
    public QuotationDTO createQuotation(QuotationDTO quotationDTO) {
        log.info("Creating quotation for company: {}", quotationDTO.getCompanyId());
        Quotation quotation = quotationMapper.toEntity(quotationDTO);
        quotation.setCreatedAt(LocalDateTime.now());
        if (quotation.getStatus() == null) {
            quotation.setStatus("DRAFT");
        }
        Quotation saved = quotationRepository.save(quotation);
        return quotationMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuotationDTO> getQuotationById(Long companyId, Long quotationId) {
        return quotationRepository.findById(quotationId)
            .filter(q -> q.getCompanyId().equals(companyId))
            .map(quotationMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDTO> getQuotationsByStatus(Long companyId, String status) {
        log.info("Fetching quotations for company: {} with status: {}", companyId, status);
        return quotationRepository.findByCompanyAndStatus(companyId, status)
            .stream()
            .map(quotationMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDTO> getQuotationsByLead(Long companyId, Long leadId) {
        log.info("Fetching quotations for company: {} and lead: {}", companyId, leadId);
        return quotationRepository.findByCompanyAndLead(companyId, leadId)
            .stream()
            .map(quotationMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDTO> getDraftQuotes(Long companyId) {
        log.info("Fetching draft quotations for company: {}", companyId);
        return quotationRepository.findDraftQuotes(companyId)
            .stream()
            .map(quotationMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDTO> getApprovedQuotes(Long companyId) {
        log.info("Fetching approved quotations for company: {}", companyId);
        return quotationRepository.findApprovedQuotes(companyId)
            .stream()
            .map(quotationMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDTO> getExpiredQuotes(Long companyId) {
        log.info("Fetching expired quotations for company: {}", companyId);
        return quotationRepository.findExpiredQuotes(companyId)
            .stream()
            .map(quotationMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDTO> getConvertibleQuotes(Long companyId) {
        log.info("Fetching convertible quotations for company: {}", companyId);
        return quotationRepository.findConvertibleQuotes(companyId)
            .stream()
            .map(quotationMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDTO> getQuotationsByDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        return quotationRepository.findByDateRange(companyId, startDate, endDate)
            .stream()
            .map(quotationMapper::toDTO)
            .toList();
    }

    @Override
    public QuotationDTO updateQuotation(Long companyId, Long quotationId, QuotationDTO quotationDTO) {
        log.info("Updating quotation: {} for company: {}", quotationId, companyId);
        Quotation quotation = quotationRepository.findById(quotationId)
            .filter(q -> q.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Quotation not found or access denied"));
        
        quotationMapper.updateEntityFromDTO(quotationDTO, quotation);
        quotation.setUpdatedAt(LocalDateTime.now());
        Quotation updated = quotationRepository.save(quotation);
        return quotationMapper.toDTO(updated);
    }

    @Override
    public QuotationDTO sendQuotation(Long companyId, Long quotationId) {
        log.info("Sending quotation: {}", quotationId);
        Quotation quotation = quotationRepository.findById(quotationId)
            .filter(q -> q.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Quotation not found or access denied"));
        
        quotation.setStatus("SENT");
        quotation.setUpdatedAt(LocalDateTime.now());
        Quotation updated = quotationRepository.save(quotation);
        return quotationMapper.toDTO(updated);
    }

    @Override
    public QuotationDTO approveQuotation(Long companyId, Long quotationId, Long approvedBy) {
        log.info("Approving quotation: {} by user: {}", quotationId, approvedBy);
        Quotation quotation = quotationRepository.findById(quotationId)
            .filter(q -> q.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Quotation not found or access denied"));
        
        quotation.setStatus("ACCEPTED");
        quotation.setApprovedBy(approvedBy != null ? approvedBy.toString() : null);
        quotation.setApprovalDate(LocalDateTime.now());
        quotation.setUpdatedAt(LocalDateTime.now());
        Quotation updated = quotationRepository.save(quotation);
        return quotationMapper.toDTO(updated);
    }

    @Override
    public QuotationDTO rejectQuotation(Long companyId, Long quotationId) {
        log.info("Rejecting quotation: {}", quotationId);
        Quotation quotation = quotationRepository.findById(quotationId)
            .filter(q -> q.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Quotation not found or access denied"));
        
        quotation.setStatus("REJECTED");
        quotation.setUpdatedAt(LocalDateTime.now());
        Quotation updated = quotationRepository.save(quotation);
        return quotationMapper.toDTO(updated);
    }

    @Override
    public Long convertQuotationToSalesOrder(Long companyId, Long quotationId, Long salesOrderId) {
        log.info("Converting quotation: {} to sales order: {}", quotationId, salesOrderId);
        Quotation quotation = quotationRepository.findById(quotationId)
            .filter(q -> q.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Quotation not found or access denied"));
        
        quotation.setStatus("CONVERTED_TO_ORDER");
        quotation.setConvertedToSalesOrderId(salesOrderId);
        quotation.setUpdatedAt(LocalDateTime.now());
        quotationRepository.save(quotation);
        return salesOrderId;
    }

    @Override
    public void deleteQuotation(Long companyId, Long quotationId) {
        log.info("Deleting quotation: {} for company: {}", quotationId, companyId);
        Quotation quotation = quotationRepository.findById(quotationId)
            .filter(q -> q.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Quotation not found or access denied"));
        quotationRepository.delete(quotation);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateAcceptedQuotesValue(Long companyId) {
        log.info("Calculating accepted quotes value for company: {}", companyId);
        return quotationRepository.calculateAcceptedQuotesValue(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByStatus(Long companyId, String status) {
        return quotationRepository.countByStatus(companyId, status);
    }
}
