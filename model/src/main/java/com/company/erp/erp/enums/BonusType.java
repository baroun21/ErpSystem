package com.company.erp.erp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BonusType {
    PERFORMANCE, HOLIDAY, REFERRAL, OTHER, PROJECT_COMPLETION;

    @JsonCreator
    public static BonusType fromValue(String value) {
        if (value == null) return null;
        return switch (value.toUpperCase().replace(" ", "_")) {
            case "PERFORMANCE" -> PERFORMANCE;
            case "HOLIDAY" -> HOLIDAY;
            case "REFERRAL" -> REFERRAL;
            case "PROJECTCOMPLETION", "PROJECT_COMPLETION" -> PROJECT_COMPLETION;
            default -> OTHER; // Fallback
        };
    }
}