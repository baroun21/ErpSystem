package com.company.erp.automation.service;

import com.company.erp.automation.repository.AutomationExecutionLogRepository;
import com.company.erp.automation.repository.AutomationRuleRepository;
import com.company.erp.erp.entites.automation.ActionType;
import com.company.erp.erp.entites.automation.AutomationExecutionLog;
import com.company.erp.erp.entites.automation.AutomationRule;
import com.company.erp.erp.entites.automation.ExecutionStatus;
import com.company.erp.erp.entites.automation.TriggerType;
import com.company.erp.shared.domain.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutomationExecutionService {

    private final AutomationRuleRepository ruleRepository;
    private final AutomationExecutionLogRepository logRepository;
    private final AutomationConditionEvaluator conditionEvaluator;
    private final ObjectMapper objectMapper;
    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    public void handleDomainEvent(DomainEvent event) {
        List<AutomationRule> rules = ruleRepository.findByEnabledTrueAndTriggerType(TriggerType.EVENT);
        if (rules.isEmpty()) {
            return;
        }

        String eventType = event.getClass().getSimpleName();
        String eventClass = event.getClass().getName();
        Map<String, Object> context = buildContext(event, eventType, eventClass);

        for (AutomationRule rule : rules) {
            if (!matchesCompany(rule, event.getTenantId())) {
                continue;
            }
            if (!matchesEvent(rule, eventType, eventClass)) {
                continue;
            }
            executeRuleAsync(rule, context, eventType);
        }
    }

    @Async
    @Transactional
    public void executeRuleAsync(AutomationRule rule, Map<String, Object> context, String triggerSource) {
        AutomationExecutionLog logEntry = AutomationExecutionLog.builder()
            .rule(rule)
            .triggerType(rule.getTriggerType())
            .triggerSource(triggerSource)
            .actionType(rule.getActionType())
            .status(ExecutionStatus.RUNNING)
            .startedAt(LocalDateTime.now())
            .payload(serializeContext(context))
            .build();

        logRepository.save(logEntry);

        try {
            if (!conditionEvaluator.matches(rule.getConditions(), context)) {
                logEntry.setStatus(ExecutionStatus.SKIPPED);
                logEntry.setMessage("Conditions not met for rule execution.");
                logEntry.setFinishedAt(LocalDateTime.now());
                logRepository.save(logEntry);
                return;
            }

            executeAction(rule, context, logEntry);
            logEntry.setStatus(ExecutionStatus.SUCCESS);
            logEntry.setFinishedAt(LocalDateTime.now());
            logRepository.save(logEntry);

            rule.setLastTriggeredAt(LocalDateTime.now());
            ruleRepository.save(rule);
        } catch (Exception ex) {
            log.error("Automation rule execution failed for rule {}", rule.getRuleId(), ex);
            logEntry.setStatus(ExecutionStatus.FAILED);
            logEntry.setErrorDetails(ex.getMessage());
            logEntry.setFinishedAt(LocalDateTime.now());
            logRepository.save(logEntry);
        }
    }

    @Async
    public void executeManual(Long ruleId) {
        ruleRepository.findById(ruleId).ifPresent(rule ->
            executeRuleAsync(rule, Map.of("manual", true), "manual")
        );
    }

    private void executeAction(AutomationRule rule, Map<String, Object> context, AutomationExecutionLog logEntry) {
        ActionType actionType = rule.getActionType();
        if (actionType == null) {
            throw new IllegalStateException("Rule has no action type");
        }

        switch (actionType) {
            case SEND_EMAIL -> executeSendEmail(rule, logEntry);
            case NOTIFY_USER -> executeNotifyUser(rule, logEntry);
            default -> throw new UnsupportedOperationException("Action not implemented: " + actionType);
        }
    }

    private void executeSendEmail(AutomationRule rule, AutomationExecutionLog logEntry) {
        Map<String, Object> payload = parsePayload(rule.getActionPayload());
        String to = stringValue(payload.get("to"));
        String subject = stringValue(payload.getOrDefault("subject", "Automation Notification"));
        String body = stringValue(payload.getOrDefault("body", "An automation rule was triggered."));

        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Email action requires 'to' in actionPayload");
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            throw new IllegalStateException("JavaMailSender is not configured");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        logEntry.setMessage("Email sent to " + to);
    }

    private void executeNotifyUser(AutomationRule rule, AutomationExecutionLog logEntry) {
        Map<String, Object> payload = parsePayload(rule.getActionPayload());
        String userId = stringValue(payload.get("userId"));
        String message = stringValue(payload.getOrDefault("message", "Automation notification"));
        String contextInfo = userId == null ? "Broadcast" : "User " + userId;
        logEntry.setMessage("Notify user: " + contextInfo + " - " + message);
    }

    private boolean matchesEvent(AutomationRule rule, String eventType, String eventClass) {
        if (rule.getTriggerEvent() == null || rule.getTriggerEvent().isBlank()) {
            return true;
        }
        String expected = rule.getTriggerEvent().trim().toLowerCase(Locale.ROOT);
        return expected.equals(eventType.toLowerCase(Locale.ROOT))
            || expected.equals(eventClass.toLowerCase(Locale.ROOT));
    }

    private boolean matchesCompany(AutomationRule rule, Long tenantId) {
        if (rule.getCompanyId() == null || rule.getCompanyId().isBlank() || tenantId == null) {
            return true;
        }
        return rule.getCompanyId().trim().equals(String.valueOf(tenantId));
    }

    private Map<String, Object> buildContext(DomainEvent event, String eventType, String eventClass) {
        Map<String, Object> context = new HashMap<>();
        context.put("eventtype", eventType);
        context.put("eventclass", eventClass);
        context.put("tenantid", event.getTenantId());
        context.put("aggregateid", event.getAggregateId());
        context.put("aggregatetype", event.getAggregateType());
        context.put("triggeredby", event.getTriggeredBy());
        return context;
    }

    private Map<String, Object> parsePayload(String payload) {
        if (payload == null || payload.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(payload, new TypeReference<>() {});
        } catch (JsonProcessingException ex) {
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("raw", payload);
            return fallback;
        }
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private String serializeContext(Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(context);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }
}

