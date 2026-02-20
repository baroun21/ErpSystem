package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.JournalEntry;
import com.company.erp.erp.entites.finance.Company;
import com.company.userService.finance.mapper.JournalEntryMapper;
import com.company.userService.finance.dto.JournalEntryCreateRequest;
import com.company.userService.finance.dto.JournalEntryResponse;
import com.company.userService.finance.repository.JournalEntryRepository;
import com.company.userService.finance.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JournalEntryService {
    private final JournalEntryRepository journalEntryRepository;
    private final CompanyRepository companyRepository;

    public JournalEntryResponse createEntry(JournalEntryCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        JournalEntry entry = JournalEntryMapper.INSTANCE.toEntity(request);
        entry.setCompany(company);
        entry.setStatus("DRAFT");
        entry.setTotalDebit(java.math.BigDecimal.ZERO);
        entry.setTotalCredit(java.math.BigDecimal.ZERO);
        
        JournalEntry saved = journalEntryRepository.save(entry);
        return JournalEntryMapper.INSTANCE.toResponse(saved);
    }

    public JournalEntryResponse getEntryById(Long id) {
        JournalEntry entry = journalEntryRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Journal entry not found with id: " + id));
        return JournalEntryMapper.INSTANCE.toResponse(entry);
    }

    public List<JournalEntryResponse> getEntriesByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return journalEntryRepository.findByCompany(company).stream()
            .map(JournalEntryMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<JournalEntryResponse> getPostedEntries(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return journalEntryRepository.findByCompanyAndStatus(company, "POSTED").stream()
            .map(JournalEntryMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public void postEntry(Long id) {
        JournalEntry entry = journalEntryRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Journal entry not found with id: " + id));
        
        // Validate entry is balanced
        if (entry.getTotalDebit().compareTo(entry.getTotalCredit()) != 0) {
            throw new RuntimeException("Entry is not balanced. Debits must equal credits.");
        }
        
        entry.setStatus("POSTED");
        journalEntryRepository.save(entry);
    }

    public JournalEntryResponse updateEntry(Long id, JournalEntryCreateRequest request) {
        JournalEntry entry = journalEntryRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Journal entry not found with id: " + id));
        
        if (!"DRAFT".equals(entry.getStatus())) {
            throw new RuntimeException("Cannot update posted entry");
        }
        
        entry.setEntryNumber(request.getEntryNumber());
        entry.setEntryDate(request.getEntryDate());
        entry.setDescription(request.getDescription());
        entry.setReferenceType(request.getReferenceType());
        entry.setReferenceId(request.getReferenceId());
        
        JournalEntry saved = journalEntryRepository.save(entry);
        return JournalEntryMapper.INSTANCE.toResponse(saved);
    }

    public void deleteEntry(Long id) {
        journalEntryRepository.deleteById(id);
    }
}
