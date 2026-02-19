package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.LeadDTO;
import java.util.List;
import java.util.Optional;

public interface LeadService {
    LeadDTO createLead(LeadDTO leadDTO);
    Optional<LeadDTO> getLeadById(Long companyId, Long leadId);
    List<LeadDTO> getLeadsByCompanyAndStatus(Long companyId, String status);
    List<LeadDTO> getLeadsBySource(Long companyId, String source);
    List<LeadDTO> getAssignedLeads(Long companyId, Long salesRepId);
    List<LeadDTO> getHighValueLeads(Long companyId, java.math.BigDecimal minValue);
    List<LeadDTO> getLeadsByDateRange(Long companyId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    LeadDTO updateLead(Long companyId, Long leadId, LeadDTO leadDTO);
    void deleteLead(Long companyId, Long leadId);
    Long countLeadsByStatus(Long companyId, String status);
    List<LeadDTO> searchLeadsByEmail(Long companyId, String email);
}
