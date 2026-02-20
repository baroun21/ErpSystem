package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.QuotationDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuotationService {
    QuotationDTO createQuotation(QuotationDTO quotationDTO);
    Optional<QuotationDTO> getQuotationById(String companyId, Long quotationId);
    List<QuotationDTO> getQuotationsByStatus(String companyId, String status);
    List<QuotationDTO> getQuotationsByLead(String companyId, Long leadId);
    List<QuotationDTO> getDraftQuotes(String companyId);
    List<QuotationDTO> getApprovedQuotes(String companyId);
    List<QuotationDTO> getExpiredQuotes(String companyId);
    List<QuotationDTO> getConvertibleQuotes(String companyId);
    List<QuotationDTO> getQuotationsByDateRange(String companyId, LocalDateTime startDate, LocalDateTime endDate);
    QuotationDTO updateQuotation(String companyId, Long quotationId, QuotationDTO quotationDTO);
    QuotationDTO sendQuotation(String companyId, Long quotationId);
    QuotationDTO approveQuotation(String companyId, Long quotationId, String approvedBy);
    QuotationDTO rejectQuotation(String companyId, Long quotationId);
    Long convertQuotationToSalesOrder(String companyId, Long quotationId, Long salesOrderId);
    void deleteQuotation(String companyId, Long quotationId);
    BigDecimal calculateAcceptedQuotesValue(String companyId);
    Long countByStatus(String companyId, String status);
}
