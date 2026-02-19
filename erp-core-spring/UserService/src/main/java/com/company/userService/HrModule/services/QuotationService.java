package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.QuotationDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuotationService {
    QuotationDTO createQuotation(QuotationDTO quotationDTO);
    Optional<QuotationDTO> getQuotationById(Long companyId, Long quotationId);
    List<QuotationDTO> getQuotationsByStatus(Long companyId, String status);
    List<QuotationDTO> getQuotationsByLead(Long companyId, Long leadId);
    List<QuotationDTO> getDraftQuotes(Long companyId);
    List<QuotationDTO> getApprovedQuotes(Long companyId);
    List<QuotationDTO> getExpiredQuotes(Long companyId);
    List<QuotationDTO> getConvertibleQuotes(Long companyId);
    List<QuotationDTO> getQuotationsByDateRange(Long companyId, LocalDate startDate, LocalDate endDate);
    QuotationDTO updateQuotation(Long companyId, Long quotationId, QuotationDTO quotationDTO);
    QuotationDTO sendQuotation(Long companyId, Long quotationId);
    QuotationDTO approveQuotation(Long companyId, Long quotationId, Long approvedBy);
    QuotationDTO rejectQuotation(Long companyId, Long quotationId);
    Long convertQuotationToSalesOrder(Long companyId, Long quotationId, Long salesOrderId);
    void deleteQuotation(Long companyId, Long quotationId);
    BigDecimal calculateAcceptedQuotesValue(Long companyId);
    Long countByStatus(Long companyId, String status);
}
