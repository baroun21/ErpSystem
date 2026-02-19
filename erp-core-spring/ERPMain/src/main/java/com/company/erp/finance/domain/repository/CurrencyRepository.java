package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query("SELECT c FROM Currency c WHERE c.companyId = :companyId")
    List<Currency> findAllByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT c FROM Currency c WHERE c.companyId = :companyId AND c.currencyCode = :code")
    Optional<Currency> findByCurrencyCodeInCompany(@Param("companyId") Long companyId, @Param("code") String code);

    @Query("SELECT c FROM Currency c WHERE c.companyId = :companyId AND c.active = true")
    List<Currency> findActiveCurrenciesInCompany(@Param("companyId") Long companyId);

    @Query("SELECT c FROM Currency c WHERE c.companyId = :companyId AND c.isBaseCurrency = true")
    Optional<Currency> findBaseCurrencyInCompany(@Param("companyId") Long companyId);
}
