package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT er FROM ExchangeRate er WHERE er.companyId = :companyId AND er.fromCurrency = :fromCurrency AND er.toCurrency = :toCurrency AND er.rateDate = :rateDate")
    Optional<ExchangeRate> findRateForDateInCompany(@Param("companyId") Long companyId, @Param("fromCurrency") String fromCurrency, @Param("toCurrency") String toCurrency, @Param("rateDate") LocalDate rateDate);

    @Query("SELECT er FROM ExchangeRate er WHERE er.companyId = :companyId AND er.fromCurrency = :fromCurrency AND er.toCurrency = :toCurrency AND er.rateDate <= :asOfDate ORDER BY er.rateDate DESC LIMIT 1")
    Optional<ExchangeRate> findLatestRateUpToDateInCompany(@Param("companyId") Long companyId, @Param("fromCurrency") String fromCurrency, @Param("toCurrency") String toCurrency, @Param("asOfDate") LocalDate asOfDate);

    @Query("SELECT er FROM ExchangeRate er WHERE er.companyId = :companyId AND er.fromCurrency = :fromCurrency AND er.toCurrency = :toCurrency AND er.rateDate BETWEEN :startDate AND :endDate ORDER BY er.rateDate DESC")
    List<ExchangeRate> findRatesInDateRangeInCompany(@Param("companyId") Long companyId, @Param("fromCurrency") String fromCurrency, @Param("toCurrency") String toCurrency, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT er FROM ExchangeRate er WHERE er.companyId = :companyId AND er.active = true ORDER BY er.rateDate DESC")
    List<ExchangeRate> findActiveRatesInCompany(@Param("companyId") Long companyId);
}
