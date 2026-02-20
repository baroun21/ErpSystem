package com.company.erp.automation.controller;

import com.company.erp.automation.service.AutomationExecutionService;
import com.company.erp.automation.service.AutomationLogService;
import com.company.erp.automation.service.AutomationRuleService;
import com.company.erp.erp.Dtos.automation.AutomationExecutionLogDTO;
import com.company.erp.erp.Dtos.automation.AutomationRuleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automation")
@RequiredArgsConstructor
public class AutomationController {

    private final AutomationRuleService ruleService;
    private final AutomationLogService logService;
    private final AutomationExecutionService executionService;

    @GetMapping("/rules")
    public ResponseEntity<List<AutomationRuleDTO>> getRules(@RequestParam(required = false) String companyId) {
        return ResponseEntity.ok(ruleService.getRules(companyId));
    }

    @GetMapping("/rules/{ruleId}")
    public ResponseEntity<AutomationRuleDTO> getRule(@PathVariable Long ruleId) {
        return ruleService.getRule(ruleId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/rules")
    public ResponseEntity<AutomationRuleDTO> createRule(@RequestBody AutomationRuleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ruleService.createRule(dto));
    }

    @PutMapping("/rules/{ruleId}")
    public ResponseEntity<AutomationRuleDTO> updateRule(
        @PathVariable Long ruleId,
        @RequestBody AutomationRuleDTO dto) {
        return ResponseEntity.ok(ruleService.updateRule(ruleId, dto));
    }

    @PutMapping("/rules/{ruleId}/toggle")
    public ResponseEntity<AutomationRuleDTO> toggleRule(
        @PathVariable Long ruleId,
        @RequestParam boolean enabled) {
        return ResponseEntity.ok(ruleService.setEnabled(ruleId, enabled));
    }

    @DeleteMapping("/rules/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long ruleId) {
        ruleService.deleteRule(ruleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rules/{ruleId}/execute")
    public ResponseEntity<Void> executeRule(@PathVariable Long ruleId) {
        executionService.executeManual(ruleId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/logs")
    public ResponseEntity<List<AutomationExecutionLogDTO>> getLogs(@RequestParam(required = false) Long ruleId) {
        return ResponseEntity.ok(logService.getLogs(ruleId));
    }

    @GetMapping("/logs/{logId}")
    public ResponseEntity<AutomationExecutionLogDTO> getLog(@PathVariable Long logId) {
        return logService.getLog(logId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
