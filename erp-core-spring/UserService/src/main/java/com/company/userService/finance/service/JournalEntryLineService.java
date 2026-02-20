package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.JournalEntryLine;
import com.company.erp.erp.entites.finance.JournalEntry;
import com.company.userService.finance.mapper.JournalEntryLineMapper;
import com.company.userService.finance.dto.JournalEntryLineCreateRequest;
import com.company.userService.finance.dto.JournalEntryLineResponse;
import com.company.userService.finance.repository.JournalEntryLineRepository;
import com.company.userService.finance.repository.JournalEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JournalEntryLineService {
    private final JournalEntryLineRepository journalEntryLineRepository;
    private final JournalEntryRepository journalEntryRepository;

    public JournalEntryLineResponse createEntryLine(JournalEntryLineCreateRequest request) {
        JournalEntry entry = journalEntryRepository.findById(request.getJournalEntryId())
            .orElseThrow(() -> new RuntimeException("Journal entry not found"));
        
        JournalEntryLine line = JournalEntryLineMapper.INSTANCE.toEntity(request);
        line.setJournalEntry(entry);
        
        // Update entry totals
        BigDecimal newDebit = entry.getTotalDebit().add(request.getDebitAmount() != null ? request.getDebitAmount() : BigDecimal.ZERO);
        BigDecimal newCredit = entry.getTotalCredit().add(request.getCreditAmount() != null ? request.getCreditAmount() : BigDecimal.ZERO);
        entry.setTotalDebit(newDebit);
        entry.setTotalCredit(newCredit);
        journalEntryRepository.save(entry);
        
        JournalEntryLine saved = journalEntryLineRepository.save(line);
        return JournalEntryLineMapper.INSTANCE.toResponse(saved);
    }

    public JournalEntryLineResponse getEntryLineById(Long id) {
        JournalEntryLine line = journalEntryLineRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Journal entry line not found with id: " + id));
        return JournalEntryLineMapper.INSTANCE.toResponse(line);
    }

    public List<JournalEntryLineResponse> getEntryLinesByEntry(Long entryId) {
        JournalEntry entry = journalEntryRepository.findById(entryId)
            .orElseThrow(() -> new RuntimeException("Journal entry not found"));
        return journalEntryLineRepository.findByJournalEntry(entry).stream()
            .map(JournalEntryLineMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public JournalEntryLineResponse updateEntryLine(Long id, JournalEntryLineCreateRequest request) {
        JournalEntryLine line = journalEntryLineRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Journal entry line not found with id: " + id));
        
        line.setDescription(request.getDescription());
        line.setDebitAmount(request.getDebitAmount());
        line.setCreditAmount(request.getCreditAmount());
        
        JournalEntryLine saved = journalEntryLineRepository.save(line);
        return JournalEntryLineMapper.INSTANCE.toResponse(saved);
    }

    public void deleteEntryLine(Long id) {
        journalEntryLineRepository.deleteById(id);
    }
}
