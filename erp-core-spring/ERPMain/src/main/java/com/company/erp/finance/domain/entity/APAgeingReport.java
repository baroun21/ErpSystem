package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * APAgeingReport
 * Accounts Payable aging by bucket showing overdue bills
 */
@Entity
@Table(name = "ap_ageing_reports", indexes = {
        @Index(name = "idx_ap_company_date", columnList = "company_id, report_date"),
        @Index(name = "idx_ap_report_date", columnList = "report_date")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APAgeingReport extends BaseEntity {

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate reportDate;

    @Column(nullable = false)
    private Long supplierId; // Would normally be @ManyToOne but omitted for external reference

    @Column(columnDefinition = "NVARCHAR2(100)")
    private String supplierName;

    // Aging buckets (0-30 days, 31-60 days, 61-90 days, 90+ days)
    @Column(precision = 15, scale = 2)
    private BigDecimal current030; // Not yet due

    @Column(precision = 15, scale = 2)
    private BigDecimal days3160; // 31-60 days overdue

    @Column(precision = 15, scale = 2)
    private BigDecimal days6190; // 61-90 days overdue

    @Column(precision = 15, scale = 2)
    private BigDecimal days90Plus; // 90+ days overdue

    @Column(precision = 15, scale = 2)
    private BigDecimal totalOutstanding; // Sum of all buckets

    @Column(columnDefinition = "NVARCHAR2(50)")
    private String paymentStatus; // ON_TIME, UPCOMING, OVERDUE, SEVERELY_OVERDUE

    @Column(columnDefinition = "NVARCHAR2(2000)")
    private String notes;

    public BigDecimal getTotalOverdue() {
        return days3160.add(days6190).add(days90Plus);
    }
}
