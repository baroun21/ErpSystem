package com.company.erp.shared.infrastructure.event;

import com.company.erp.shared.domain.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Async listener that persists domain events for audit/event sourcing.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DomainEventPersistenceListener {

    private final DomainEventRecordRepository repository;
    private final ObjectMapper objectMapper;

    @Async
    @EventListener
    public void handle(DomainEvent event) {
        if (event.getTenantId() == null) {
            log.warn("Skipping DomainEvent persistence: missing tenantId for {}", event.getClass().getSimpleName());
            return;
        }

        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to serialize DomainEvent payload: {}", event.getClass().getSimpleName(), ex);
            payload = "{\"serializationError\":true}";
        }

        DomainEventRecord record = DomainEventRecord.builder()
            .companyId(event.getTenantId())
            .eventId(event.getEventId())
            .eventType(event.getClass().getSimpleName())
            .aggregateId(event.getAggregateId())
            .aggregateType(event.getAggregateType())
            .payload(payload)
            .triggeredBy(event.getTriggeredBy())
            .occurredAt(event.getOccurredAt())
            .build();

        repository.save(record);
    }
}

