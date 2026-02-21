package com.company.erp.automation.repository;

import com.company.erp.erp.entites.automation.AutomationRule;
import com.company.erp.erp.entites.automation.TriggerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutomationRuleRepository extends JpaRepository<AutomationRule, Long> {
    List<AutomationRule> findByCompanyIdOrderByUpdatedAtDesc(String companyId);

    List<AutomationRule> findByEnabledTrueAndTriggerType(TriggerType triggerType);
}

