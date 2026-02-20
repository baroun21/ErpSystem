package com.company.erp.shared.infrastructure.event.listener;

import com.company.erp.commandcenter.service.SnapshotCacheService;
import com.company.erp.shared.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Listens to all domain events and invalidates the command center snapshot cache
 * so it refreshes on the next read.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommandCenterCacheUpdater {

    private final SnapshotCacheService snapshotCacheService;

    @Async
    @EventListener
    public void onDomainEvent(DomainEvent event) {
        try {
            log.info("Domain event received: {}. Invalidating snapshot cache for company: {}", 
                    event.getClass().getSimpleName(), event.getTenantId());
            
            // Clear cache for this company so next read triggers refresh
            snapshotCacheService.clearCache(String.valueOf(event.getTenantId()));
            
            log.info("Snapshot cache cleared for company: {}", event.getTenantId());
        } catch (Exception e) {
            log.error("Error updating command center cache for company {}: {}", 
                    event.getTenantId(), e.getMessage(), e);
        }
    }
}
