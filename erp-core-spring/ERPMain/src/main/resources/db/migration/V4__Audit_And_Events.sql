-- ============================================================================
-- NEXORA ERP - Audit and Event Sourcing
-- Tracks all domain events and changes to entities
-- ============================================================================

-- Domain Events (Event Sourcing)
CREATE TABLE IF NOT EXISTS domain_events (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    event_id VARCHAR(36) UNIQUE,  -- UUID
    event_type VARCHAR(255) NOT NULL,  -- EmployeeHiredEvent, InvoiceCreatedEvent, etc.
    aggregate_id BIGINT,  -- ID of the primary entity
    aggregate_type VARCHAR(100),  -- Employee, Invoice, etc.
    payload JSONB,  -- Full event data as JSON
    triggered_by VARCHAR(255),  -- User who triggered
    occurred_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(company_id) REFERENCES companies(id)
);

-- Audit Log (Track all changes)
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    entity_type VARCHAR(100),  -- Employee, Invoice, etc.
    entity_id BIGINT,
    action VARCHAR(50),  -- CREATE, UPDATE, DELETE
    old_values JSONB,  -- Previous values
    new_values JSONB,  -- Updated values
    changed_fields TEXT[],  -- Array of field names changed
    changed_by VARCHAR(255),
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(company_id) REFERENCES companies(id)
);

-- Audit Indexes for performance
CREATE INDEX IF NOT EXISTS idx_domain_events_company_id ON domain_events(company_id);
CREATE INDEX IF NOT EXISTS idx_domain_events_aggregate_id ON domain_events(aggregate_id);
CREATE INDEX IF NOT EXISTS idx_domain_events_event_type ON domain_events(event_type);
CREATE INDEX IF NOT EXISTS idx_audit_log_company_id ON audit_log(company_id);
CREATE INDEX IF NOT EXISTS idx_audit_log_entity ON audit_log(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_log_timestamp ON audit_log(changed_at DESC);
