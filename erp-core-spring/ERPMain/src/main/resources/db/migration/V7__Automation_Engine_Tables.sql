-- ============================================================================
-- NEXORA ERP - Automation Engine Tables
-- Event-based and time-based automation rules with execution logs
-- ============================================================================

CREATE TABLE IF NOT EXISTS automation_rules (
    rule_id BIGSERIAL PRIMARY KEY,
    company_id VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    trigger_type VARCHAR(30) NOT NULL,
    trigger_event VARCHAR(255),
    schedule_expression VARCHAR(255),
    schedule_timezone VARCHAR(60),
    enabled BOOLEAN DEFAULT TRUE,
    action_type VARCHAR(50) NOT NULL,
    action_payload TEXT,
    last_triggered_at TIMESTAMP,
    next_run_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS automation_conditions (
    condition_id BIGSERIAL PRIMARY KEY,
    rule_id BIGINT NOT NULL,
    field VARCHAR(100) NOT NULL,
    operator VARCHAR(30) NOT NULL,
    value VARCHAR(500),
    FOREIGN KEY(rule_id) REFERENCES automation_rules(rule_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS automation_execution_logs (
    log_id BIGSERIAL PRIMARY KEY,
    rule_id BIGINT NOT NULL,
    trigger_type VARCHAR(30) NOT NULL,
    trigger_source VARCHAR(255),
    action_type VARCHAR(50),
    status VARCHAR(30),
    message TEXT,
    error_details TEXT,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    payload TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(rule_id) REFERENCES automation_rules(rule_id)
);

CREATE INDEX IF NOT EXISTS idx_automation_rules_company ON automation_rules(company_id);
CREATE INDEX IF NOT EXISTS idx_automation_rules_trigger ON automation_rules(trigger_type);
CREATE INDEX IF NOT EXISTS idx_automation_rules_enabled ON automation_rules(enabled);
CREATE INDEX IF NOT EXISTS idx_automation_conditions_rule ON automation_conditions(rule_id);
CREATE INDEX IF NOT EXISTS idx_automation_logs_rule ON automation_execution_logs(rule_id);
CREATE INDEX IF NOT EXISTS idx_automation_logs_status ON automation_execution_logs(status);
