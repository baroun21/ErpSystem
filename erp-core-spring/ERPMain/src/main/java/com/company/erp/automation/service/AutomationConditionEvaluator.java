package com.company.erp.automation.service;

import com.company.erp.erp.entites.automation.AutomationCondition;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class AutomationConditionEvaluator {

    public boolean matches(List<AutomationCondition> conditions, Map<String, Object> context) {
        if (conditions == null || conditions.isEmpty()) {
            return true;
        }

        if (context == null || context.isEmpty()) {
            return false;
        }

        for (AutomationCondition condition : conditions) {
            if (!matchesCondition(condition, context)) {
                return false;
            }
        }

        return true;
    }

    private boolean matchesCondition(AutomationCondition condition, Map<String, Object> context) {
        String field = normalize(condition.getField());
        String operator = normalize(condition.getOperator());
        String expected = condition.getValue() == null ? "" : condition.getValue();

        Object actualValue = context.get(field);
        if (actualValue == null) {
            return false;
        }

        String actual = String.valueOf(actualValue);

        return switch (operator) {
            case "EQUALS", "EQ" -> actual.equalsIgnoreCase(expected);
            case "NOT_EQUALS", "NEQ" -> !actual.equalsIgnoreCase(expected);
            case "CONTAINS" -> actual.toLowerCase(Locale.ROOT).contains(normalize(expected));
            case "STARTS_WITH" -> actual.toLowerCase(Locale.ROOT).startsWith(normalize(expected));
            case "ENDS_WITH" -> actual.toLowerCase(Locale.ROOT).endsWith(normalize(expected));
            case "GREATER_THAN", "GT" -> compareNumbers(actual, expected) > 0;
            case "LESS_THAN", "LT" -> compareNumbers(actual, expected) < 0;
            default -> false;
        };
    }

    private int compareNumbers(String actual, String expected) {
        try {
            BigDecimal actualNumber = new BigDecimal(actual);
            BigDecimal expectedNumber = new BigDecimal(expected);
            return actualNumber.compareTo(expectedNumber);
        } catch (NumberFormatException ex) {
            return actual.compareTo(expected);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}

