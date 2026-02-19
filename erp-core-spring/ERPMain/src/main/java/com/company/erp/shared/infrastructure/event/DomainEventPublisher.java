package com.company.erp.shared.infrastructure.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Application-wide event publisher for domain events.
 * Publishes domain events via Spring's ApplicationEventPublisher,
 * which invokes all @EventListener methods.
 *
 * Usage:
 *   eventPublisher.publish(new EmployeeHiredEvent(...))
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publish a domain event
     */
    public void publish(DomainEvent event) {
        log.info("Publishing domain event: {} for aggregate ID: {}", 
                event.getClass().getSimpleName(), 
                event.getAggregateId());

        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publish multiple events (e.g., when aggregate creates multiple events)
     */
    public void publishAll(Iterable<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
