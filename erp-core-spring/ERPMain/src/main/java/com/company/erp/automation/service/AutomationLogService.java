package com.company.erp.automation.service;

import com.company.erp.automation.repository.AutomationExecutionLogRepository;
import com.company.erp.erp.Dtos.automation.AutomationExecutionLogDTO;
import com.company.erp.erp.mapper.automation.AutomationExecutionLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutomationLogService {

    private final AutomationExecutionLogRepository logRepository;
    private final AutomationExecutionLogMapper logMapper;

    public List<AutomationExecutionLogDTO> getLogs(Long ruleId) {
        if (ruleId != null) {
            return logRepository.findByRule_RuleIdOrderByStartedAtDesc(ruleId)
                .stream()
                .map(logMapper::toDTO)
                .toList();
        }

        return logRepository.findAll()
            .stream()
            .map(logMapper::toDTO)
            .toList();
    }

    public Optional<AutomationExecutionLogDTO> getLog(Long logId) {
        return logRepository.findById(logId).map(logMapper::toDTO);
    }
}

