package com.company.userService.finance.controller;

import com.company.userService.finance.dto.JournalEntryCreateRequest;
import com.company.userService.finance.dto.JournalEntryResponse;
import com.company.userService.finance.service.JournalEntryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal-entries")
@AllArgsConstructor
public class JournalEntryController {
    private final JournalEntryService journalEntryService;

    @PostMapping
    public ResponseEntity<JournalEntryResponse> createEntry(@RequestBody JournalEntryCreateRequest request) {
        return ResponseEntity.ok(journalEntryService.createEntry(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntryResponse> getEntryById(@PathVariable Long id) {
        return ResponseEntity.ok(journalEntryService.getEntryById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JournalEntryResponse>> getEntriesByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(journalEntryService.getEntriesByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/posted")
    public ResponseEntity<List<JournalEntryResponse>> getPostedEntries(@PathVariable Long companyId) {
        return ResponseEntity.ok(journalEntryService.getPostedEntries(companyId));
    }

    @PostMapping("/{id}/post")
    public ResponseEntity<Void> postEntry(@PathVariable Long id) {
        journalEntryService.postEntry(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntryResponse> updateEntry(
        @PathVariable Long id,
        @RequestBody JournalEntryCreateRequest request) {
        return ResponseEntity.ok(journalEntryService.updateEntry(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        journalEntryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
