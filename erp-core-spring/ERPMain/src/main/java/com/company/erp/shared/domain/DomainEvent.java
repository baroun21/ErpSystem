package com.company.erp.shared.domain;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base class for all domain events.
 * Domain events represent something that happened in the business domain.
 * They are published via Spring ApplicationEventPublisher and handled by @EventListener methods.
 *
 * Example: EmployeeHiredEvent, InvoiceCreatedEvent
 */
@Data
public abstract class DomainEvent extends ApplicationEvent {

    /**
     * Unique event identifier for idempotency
     */
    private String eventId = UUID.randomUUID().toString();

    /**
     * Tenant (company) that triggered this event
     */
    private Long tenantId;

    /**
     * Aggregate root ID that this event relates to
     */
    private Long aggregateId;

    /**
     * Type of aggregate (Employee, Invoice, etc.)
     */
    private String aggregateType;

    /**
     * When this event occurred
     */
    private LocalDateTime occurredAt = LocalDateTime.now();

    /**
     * User who triggered this event
     */
    private String triggeredBy;

    /**
     * Protected no-arg constructor for Lombok-generated constructors
     * Subclasses must call one of the explicit constructors
     */
    protected DomainEvent() {
        super("domain-event");  // Default source for ApplicationEvent
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = LocalDateTime.now();
    }

    public DomainEvent(Object source) {
        super(source);
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = LocalDateTime.now();
    }

    public DomainEvent(Long aggregateId, String aggregateType, Long tenantId, String triggeredBy) {
        super(aggregateId != null ? aggregateId : "domain-event");
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.tenantId = tenantId;
        this.triggeredBy = triggeredBy;
        this.occurredAt = LocalDateTime.now();
    }
}

