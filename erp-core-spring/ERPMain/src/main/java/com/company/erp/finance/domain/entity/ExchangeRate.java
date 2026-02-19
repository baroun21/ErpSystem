package com.company.erp.finance.domain.entity;

import com.company.erp.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Exchange Rate entity
 * Tracks currency exchange rates for multi-currency transactions
 */
@Entity
@Table(name = "exchange_rates", indexes = {
    @Index(name = "idx_er_company", columnList = "company_id"),
    @Index(name = "idx_er_date", columnList = "rate_date"),
    @Index(name = "idx_er_from_to", columnList = "from_currency,to_currency")
})
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ExchangeRate extends BaseEntity {

    @Column(name = "from_currency", length = 3, nullable = false)
    private String fromCurrency;

    @Column(name = "to_currency", length = 3, nullable = false)
    private String toCurrency;

    @Column(name = "rate_date", nullable = false)
    private LocalDate rateDate;

    @Column(name = "rate", precision = 18, scale = 8, nullable = false)
    private BigDecimal rate;

    @Column(name = "inverse_rate", precision = 18, scale = 8)
    private BigDecimal inverseRate;

    @Column(name = "source", length = 100)
    private String source; // ECB, Fed, etc.

    @Column(name = "is_manual")
    private Boolean isManual = Boolean.FALSE;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    public ExchangeRate() {
    }

    public void calculateInverseRate() {
        if (rate != null && rate.compareTo(BigDecimal.ZERO) > 0) {
            this.inverseRate = BigDecimal.ONE.divide(rate, 8, java.math.RoundingMode.HALF_UP);
        }
    }
}
