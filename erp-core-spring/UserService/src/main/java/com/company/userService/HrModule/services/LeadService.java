package com.company.userService.HrModule.services;

import com.company.erp.erp.Dtos.sales.LeadDTO;
import java.util.List;
import java.util.Optional;

public interface LeadService {
    LeadDTO createLead(LeadDTO leadDTO);
    Optional<LeadDTO> getLeadById(String companyId, Long leadId);
    List<LeadDTO> getLeadsByStatus(String companyId, String status);
    List<LeadDTO> getLeadsBySource(String companyId, String source);
    List<LeadDTO> getLeadsByAssignedTo(String companyId, String assignedTo);
    List<LeadDTO> getHighValueLeads(String companyId, java.math.BigDecimal minValue);
    List<LeadDTO> getLeadsByDateRange(String companyId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    LeadDTO updateLead(String companyId, Long leadId, LeadDTO leadDTO);
    void deleteLead(String companyId, Long leadId);
    Long countLeadsByStatus(String companyId, String status);
    List<LeadDTO> searchLeadsByEmail(String companyId, String email);
}
