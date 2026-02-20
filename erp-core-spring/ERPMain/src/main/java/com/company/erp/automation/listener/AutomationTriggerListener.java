package com.company.erp.automation.listener;

import com.company.erp.automation.service.AutomationExecutionService;
import com.company.erp.shared.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutomationTriggerListener {

    private final AutomationExecutionService executionService;

    @EventListener
    public void onDomainEvent(DomainEvent event) {
        log.debug("AutomationTriggerListener received event: {}", event.getClass().getSimpleName());
        executionService.handleDomainEvent(event);
    }
}
