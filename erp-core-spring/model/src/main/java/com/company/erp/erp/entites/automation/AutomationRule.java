package com.company.erp.erp.entites.automation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "automation_rules", indexes = {
    @Index(name = "idx_automation_rules_company", columnList = "company_id"),
    @Index(name = "idx_automation_rules_trigger", columnList = "trigger_type"),
    @Index(name = "idx_automation_rules_enabled", columnList = "enabled")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationRule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "company_id", length = 50, nullable = false)
    private String companyId;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", length = 30, nullable = false)
    private TriggerType triggerType;

    @Column(name = "trigger_event", length = 255)
    private String triggerEvent;

    @Column(name = "schedule_expression", length = 255)
    private String scheduleExpression;

    @Column(name = "schedule_timezone", length = 60)
    private String scheduleTimezone;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", length = 50, nullable = false)
    private ActionType actionType;

    @Column(name = "action_payload", columnDefinition = "TEXT")
    private String actionPayload;

    @Column(name = "last_triggered_at")
    private LocalDateTime lastTriggeredAt;

    @Column(name = "next_run_at")
    private LocalDateTime nextRunAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AutomationCondition> conditions = new ArrayList<>();
}
