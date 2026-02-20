package com.company.erp.automation.service;

import com.company.erp.automation.repository.AutomationRuleRepository;
import com.company.erp.erp.Dtos.automation.AutomationConditionDTO;
import com.company.erp.erp.Dtos.automation.AutomationRuleDTO;
import com.company.erp.erp.entites.automation.AutomationCondition;
import com.company.erp.erp.entites.automation.AutomationRule;
import com.company.erp.erp.entites.automation.TriggerType;
import com.company.erp.erp.mapper.automation.AutomationConditionMapper;
import com.company.erp.erp.mapper.automation.AutomationRuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AutomationRuleService {

    private final AutomationRuleRepository ruleRepository;
    private final AutomationRuleMapper ruleMapper;
    private final AutomationConditionMapper conditionMapper;

    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getRules(String companyId) {
        List<AutomationRule> rules = companyId == null || companyId.isBlank()
            ? ruleRepository.findAll()
            : ruleRepository.findByCompanyIdOrderByUpdatedAtDesc(companyId);

        return rules.stream().map(ruleMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<AutomationRuleDTO> getRule(Long ruleId) {
        return ruleRepository.findById(ruleId).map(ruleMapper::toDTO);
    }

    public AutomationRuleDTO createRule(AutomationRuleDTO dto) {
        AutomationRule rule = new AutomationRule();
        applyRuleFields(rule, dto);
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        replaceConditions(rule, dto.getConditions());
        AutomationRule saved = ruleRepository.save(rule);
        return ruleMapper.toDTO(saved);
    }

    public AutomationRuleDTO updateRule(Long ruleId, AutomationRuleDTO dto) {
        AutomationRule rule = ruleRepository.findById(ruleId)
            .orElseThrow(() -> new IllegalArgumentException("Automation rule not found"));

        applyRuleFields(rule, dto);
        rule.setUpdatedAt(LocalDateTime.now());

        if (dto.getConditions() != null) {
            replaceConditions(rule, dto.getConditions());
        }

        return ruleMapper.toDTO(ruleRepository.save(rule));
    }

    public AutomationRuleDTO setEnabled(Long ruleId, boolean enabled) {
        AutomationRule rule = ruleRepository.findById(ruleId)
            .orElseThrow(() -> new IllegalArgumentException("Automation rule not found"));
        rule.setEnabled(enabled);
        rule.setUpdatedAt(LocalDateTime.now());
        return ruleMapper.toDTO(ruleRepository.save(rule));
    }

    public void deleteRule(Long ruleId) {
        ruleRepository.deleteById(ruleId);
    }

    @Transactional(readOnly = true)
    public List<AutomationRule> getActiveRulesByTriggerType(TriggerType triggerType) {
        return ruleRepository.findByEnabledTrueAndTriggerType(triggerType);
    }

    private void applyRuleFields(AutomationRule rule, AutomationRuleDTO dto) {
        if (dto.getCompanyId() != null) {
            rule.setCompanyId(dto.getCompanyId());
        }
        if (dto.getName() != null) {
            rule.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            rule.setDescription(dto.getDescription());
        }
        if (dto.getTriggerType() != null) {
            rule.setTriggerType(dto.getTriggerType());
        }
        if (dto.getTriggerEvent() != null) {
            rule.setTriggerEvent(dto.getTriggerEvent());
        }
        if (dto.getScheduleExpression() != null) {
            rule.setScheduleExpression(dto.getScheduleExpression());
        }
        if (dto.getScheduleTimezone() != null) {
            rule.setScheduleTimezone(dto.getScheduleTimezone());
        }
        if (dto.getEnabled() != null) {
            rule.setEnabled(dto.getEnabled());
        }
        if (dto.getActionType() != null) {
            rule.setActionType(dto.getActionType());
        }
        if (dto.getActionPayload() != null) {
            rule.setActionPayload(dto.getActionPayload());
        }
        if (dto.getLastTriggeredAt() != null) {
            rule.setLastTriggeredAt(dto.getLastTriggeredAt());
        }
        if (dto.getNextRunAt() != null) {
            rule.setNextRunAt(dto.getNextRunAt());
        }
    }

    private void replaceConditions(AutomationRule rule, List<AutomationConditionDTO> conditionDTOs) {
        rule.getConditions().clear();
        if (conditionDTOs == null) {
            return;
        }
        List<AutomationCondition> conditions = new ArrayList<>();
        for (AutomationConditionDTO dto : conditionDTOs) {
            AutomationCondition condition = conditionMapper.toEntity(dto);
            condition.setRule(rule);
            conditions.add(condition);
        }
        rule.getConditions().addAll(conditions);
    }
}
