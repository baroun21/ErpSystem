package com.company.erp.commandcenter.controller;

import com.company.erp.commandcenter.domain.CommandCenterSnapshot;
import com.company.erp.commandcenter.service.SnapshotCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/command-center")
@RequiredArgsConstructor
@Slf4j
public class CommandCenterController {

    private final SnapshotCacheService snapshotCacheService;

    /**
     * Get the current command center snapshot for a company
     * Returns cached snapshot or refreshes if needed
     */
    @GetMapping("/{companyId}")
    public ResponseEntity<CommandCenterSnapshot> getSnapshot(@PathVariable String companyId) {
        log.info("Fetching command center snapshot for company: {}", companyId);
        CommandCenterSnapshot snapshot = snapshotCacheService.getSnapshot(companyId);
        return ResponseEntity.ok(snapshot);
    }

    /**
     * Force refresh the snapshot (useful for manual updates)
     */
    @PostMapping("/{companyId}/refresh")
    public ResponseEntity<CommandCenterSnapshot> refreshSnapshot(@PathVariable String companyId) {
        log.info("Refreshing command center snapshot for company: {}", companyId);
        CommandCenterSnapshot snapshot = snapshotCacheService.refreshSnapshot(companyId);
        return ResponseEntity.ok(snapshot);
    }

    /**
     * Clear cache for a company (debugging/testing)
     */
    @DeleteMapping("/{companyId}/cache")
    public ResponseEntity<Void> clearCache(@PathVariable String companyId) {
        log.info("Clearing cache for company: {}", companyId);
        snapshotCacheService.clearCache(companyId);
        return ResponseEntity.noContent().build();
    }
}
