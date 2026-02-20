package com.company.erp.automation.repository;

import com.company.erp.erp.entites.automation.AutomationExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutomationExecutionLogRepository extends JpaRepository<AutomationExecutionLog, Long> {
    List<AutomationExecutionLog> findByRule_RuleIdOrderByStartedAtDesc(Long ruleId);
}
