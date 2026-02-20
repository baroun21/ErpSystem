package com.company.erp.cashintelligence.service;

import com.company.erp.erp.Dtos.cash.ScenarioSimulationRequestDTO;
import com.company.erp.erp.Dtos.cash.ScenarioSimulationResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ScenarioSimulationService {

    private final CashPositionService cashPositionService;
    private final ForecastService forecastService;
    private final BurnRateService burnRateService;

    public ScenarioSimulationResultDTO simulate(ScenarioSimulationRequestDTO request) {
        BigDecimal inflowMultiplier = safe(request.getInflowMultiplier(), BigDecimal.ONE);
        BigDecimal outflowMultiplier = safe(request.getOutflowMultiplier(), BigDecimal.ONE);

        BigDecimal currentCash = cashPositionService.getCashPosition(request.getCompanyId()).getTotalCash();
        var forecast = forecastService.buildForecast(request.getCompanyId(), currentCash);
        var burnRateSnapshot = burnRateService.calculateBurnRate(request.getCompanyId());

        BigDecimal inflow = forecast.getExpectedInflow().multiply(inflowMultiplier);
        BigDecimal outflow = forecast.getExpectedOutflow().multiply(outflowMultiplier);

        BigDecimal projection30 = currentCash
            .add(forecast.getProjection30().subtract(currentCash).multiply(inflowMultiplier))
            .subtract(forecast.getExpectedOutflow().multiply(outflowMultiplier).subtract(forecast.getExpectedOutflow()));
        BigDecimal projection60 = currentCash
            .add(forecast.getProjection60().subtract(currentCash).multiply(inflowMultiplier))
            .subtract(forecast.getExpectedOutflow().multiply(outflowMultiplier).subtract(forecast.getExpectedOutflow()));
        BigDecimal projection90 = currentCash
            .add(forecast.getProjection90().subtract(currentCash).multiply(inflowMultiplier))
            .subtract(forecast.getExpectedOutflow().multiply(outflowMultiplier).subtract(forecast.getExpectedOutflow()));

        BigDecimal burnRateMonthly = burnRateSnapshot.getBurnRateMonthly();
        BigDecimal runwayDays = burnRateMonthly.signum() == 0
            ? BigDecimal.ZERO
            : currentCash.divide(burnRateMonthly, 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("30"));

        return ScenarioSimulationResultDTO.builder()
            .expectedInflow(inflow)
            .expectedOutflow(outflow)
            .projection30(projection30)
            .projection60(projection60)
            .projection90(projection90)
            .cashRunwayDays(runwayDays)
            .build();
    }

    private BigDecimal safe(BigDecimal value, BigDecimal fallback) {
        return value == null ? fallback : value;
    }
}
