package com.company.erp.erp.entites.automation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "automation_conditions", indexes = {
    @Index(name = "idx_automation_conditions_rule", columnList = "rule_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationCondition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id")
    private Long conditionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private AutomationRule rule;

    @Column(name = "field", length = 100, nullable = false)
    private String field;

    @Column(name = "operator", length = 30, nullable = false)
    private String operator;

    @Column(name = "value", length = 500)
    private String value;
}
