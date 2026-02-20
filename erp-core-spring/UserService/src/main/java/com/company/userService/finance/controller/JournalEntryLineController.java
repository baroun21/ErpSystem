package com.company.userService.finance.controller;

import com.company.userService.finance.dto.JournalEntryLineCreateRequest;
import com.company.userService.finance.dto.JournalEntryLineResponse;
import com.company.userService.finance.service.JournalEntryLineService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal-entry-lines")
@AllArgsConstructor
public class JournalEntryLineController {
    private final JournalEntryLineService journalEntryLineService;

    @PostMapping
    public ResponseEntity<JournalEntryLineResponse> createEntryLine(@RequestBody JournalEntryLineCreateRequest request) {
        return ResponseEntity.ok(journalEntryLineService.createEntryLine(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntryLineResponse> getEntryLineById(@PathVariable Long id) {
        return ResponseEntity.ok(journalEntryLineService.getEntryLineById(id));
    }

    @GetMapping("/entry/{entryId}")
    public ResponseEntity<List<JournalEntryLineResponse>> getEntryLinesByEntry(@PathVariable Long entryId) {
        return ResponseEntity.ok(journalEntryLineService.getEntryLinesByEntry(entryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntryLineResponse> updateEntryLine(
        @PathVariable Long id,
        @RequestBody JournalEntryLineCreateRequest request) {
        return ResponseEntity.ok(journalEntryLineService.updateEntryLine(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntryLine(@PathVariable Long id) {
        journalEntryLineService.deleteEntryLine(id);
        return ResponseEntity.noContent().build();
    }
}
