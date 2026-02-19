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

/**
 * Currency entity
 * Represents supported currencies for multi-currency transactions
 */
@Entity
@Table(name = "currencies", indexes = {
    @Index(name = "idx_curr_company", columnList = "company_id"),
    @Index(name = "idx_curr_code", columnList = "currency_code")
})
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Currency extends BaseEntity {

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode; // USD, EUR, GBP, JPY, etc.

    @Column(name = "currency_name", length = 100, nullable = false)
    private String currencyName;

    @Column(name = "symbol", length = 10)
    private String symbol;

    @Column(name = "is_base_currency")
    private Boolean isBaseCurrency = Boolean.FALSE; // Company's base currency

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    public Currency() {
    }
}
