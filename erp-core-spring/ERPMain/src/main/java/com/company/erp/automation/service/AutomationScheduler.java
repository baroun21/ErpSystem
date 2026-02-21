package com.company.erp.automation.service;

import com.company.erp.automation.repository.AutomationRuleRepository;
import com.company.erp.erp.entites.automation.AutomationRule;
import com.company.erp.erp.entites.automation.TriggerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutomationScheduler {

    private final AutomationRuleRepository ruleRepository;
    private final AutomationExecutionService executionService;

    @Scheduled(fixedDelayString = "${automation.scheduler.interval-ms:60000}")
    public void runScheduledRules() {
        List<AutomationRule> rules = ruleRepository.findByEnabledTrueAndTriggerType(TriggerType.TIME);
        if (rules.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        for (AutomationRule rule : rules) {
            LocalDateTime nextRun = ensureNextRun(rule, now);
            if (nextRun != null && !nextRun.isAfter(now)) {
                log.debug("Executing scheduled automation rule {}", rule.getRuleId());
                executionService.executeRuleAsync(rule, null, "scheduler");
                rule.setLastTriggeredAt(now);
                rule.setNextRunAt(calculateNextRun(rule, now));
                ruleRepository.save(rule);
            }
        }
    }

    private LocalDateTime ensureNextRun(AutomationRule rule, LocalDateTime now) {
        if (rule.getNextRunAt() != null) {
            return rule.getNextRunAt();
        }
        LocalDateTime nextRun = calculateNextRun(rule, now);
        rule.setNextRunAt(nextRun);
        ruleRepository.save(rule);
        return nextRun;
    }

    private LocalDateTime calculateNextRun(AutomationRule rule, LocalDateTime from) {
        String expression = rule.getScheduleExpression();
        if (expression == null || expression.isBlank()) {
            return null;
        }

        try {
            CronExpression cron = CronExpression.parse(expression.trim());
            ZoneId zoneId = rule.getScheduleTimezone() == null || rule.getScheduleTimezone().isBlank()
                ? ZoneId.systemDefault()
                : ZoneId.of(rule.getScheduleTimezone());
            var next = cron.next(from.atZone(zoneId));
            return next == null ? null : next.toLocalDateTime();
        } catch (Exception ex) {
            log.warn("Invalid cron expression for rule {}: {}", rule.getRuleId(), expression);
            return null;
        }
    }
}

