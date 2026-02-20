package com.company.erp.cashintelligence.service;

import com.company.erp.erp.Dtos.cash.CashForecastDTO;
import com.company.erp.erp.Dtos.cash.CashIntelligenceJobDTO;
import com.company.erp.erp.Dtos.cash.CashIntelligenceJobStatus;
import com.company.erp.erp.Dtos.cash.CashIntelligenceSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CashIntelligenceJobService {

    private final CashPositionService cashPositionService;
    private final ForecastService forecastService;
    private final BurnRateService burnRateService;
    private final ReceivableRiskService receivableRiskService;

    private final Map<String, CashIntelligenceJobDTO> jobs = new ConcurrentHashMap<>();

    public CashIntelligenceJobDTO startJob(Long companyId) {
        String jobId = UUID.randomUUID().toString();
        CashIntelligenceJobDTO job = CashIntelligenceJobDTO.builder()
            .jobId(jobId)
            .status(CashIntelligenceJobStatus.PENDING)
            .startedAt(LocalDateTime.now())
            .build();

        jobs.put(jobId, job);
        runJobAsync(jobId, companyId);
        return job;
    }

    public Optional<CashIntelligenceJobDTO> getJob(String jobId) {
        return Optional.ofNullable(jobs.get(jobId));
    }

    @Async
    public void runJobAsync(String jobId, Long companyId) {
        CashIntelligenceJobDTO job = jobs.get(jobId);
        if (job == null) {
            return;
        }

        job.setStatus(CashIntelligenceJobStatus.RUNNING);
        try {
            var cashPosition = cashPositionService.getCashPosition(companyId);
            var forecast = forecastService.buildForecast(companyId, cashPosition.getTotalCash());
            var burnRate = burnRateService.calculateBurnRate(companyId);
            var receivableRisk = receivableRiskService.buildReceivableRisk(companyId);

            CashForecastDTO forecastWithRunway = CashForecastDTO.builder()
                .expectedInflow(forecast.getExpectedInflow())
                .expectedOutflow(forecast.getExpectedOutflow())
                .projection30(forecast.getProjection30())
                .projection60(forecast.getProjection60())
                .projection90(forecast.getProjection90())
                .cashRunwayDays(calculateRunway(cashPosition.getTotalCash(), burnRate.getBurnRateMonthly()))
                .build();

            CashIntelligenceSummaryDTO summary = CashIntelligenceSummaryDTO.builder()
                .cashPosition(cashPosition)
                .forecast(forecastWithRunway)
                .burnRate(burnRate)
                .receivableRisk(receivableRisk)
                .build();

            job.setSummary(summary);
            job.setStatus(CashIntelligenceJobStatus.COMPLETE);
        } catch (Exception ex) {
            job.setStatus(CashIntelligenceJobStatus.FAILED);
            job.setError(ex.getMessage());
        } finally {
            job.setFinishedAt(LocalDateTime.now());
        }
    }

    private java.math.BigDecimal calculateRunway(java.math.BigDecimal cash, java.math.BigDecimal burnRateMonthly) {
        if (burnRateMonthly == null || burnRateMonthly.signum() == 0) {
            return java.math.BigDecimal.ZERO;
        }
        return cash.divide(burnRateMonthly, 2, java.math.RoundingMode.HALF_UP)
            .multiply(new java.math.BigDecimal("30"));
    }
}
