package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * TrialBalanceReport
 * Snapshot of trial balance report showing all account balances
 */
@Entity
@Table(name = "trial_balance_reports", indexes = {
        @Index(name = "idx_tbr_company_date", columnList = "company_id, report_date"),
        @Index(name = "idx_tbr_report_date", columnList = "report_date")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialBalanceReport extends BaseEntity {

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate reportDate;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalDebits;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalCredits;

    @Column(columnDefinition = "NVARCHAR2(1)")
    private String status; // BALANCED, OUT_OF_BALANCE

    @Column(columnDefinition = "NVARCHAR2(500)")
    private String difference; // Shows imbalance if any

    @Column(columnDefinition = "NVARCHAR2(2000)")
    private String notes;
}
