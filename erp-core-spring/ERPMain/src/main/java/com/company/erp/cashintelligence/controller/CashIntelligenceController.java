package com.company.erp.cashintelligence.controller;

import com.company.erp.cashintelligence.service.CashIntelligenceJobService;
import com.company.erp.cashintelligence.service.ScenarioSimulationService;
import com.company.erp.erp.Dtos.cash.CashIntelligenceJobDTO;
import com.company.erp.erp.Dtos.cash.ScenarioSimulationRequestDTO;
import com.company.erp.erp.Dtos.cash.ScenarioSimulationResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cash-intelligence")
@RequiredArgsConstructor
public class CashIntelligenceController {

    private final CashIntelligenceJobService jobService;
    private final ScenarioSimulationService scenarioSimulationService;

    @PostMapping("/jobs")
    public ResponseEntity<CashIntelligenceJobDTO> startJob(@RequestParam Long companyId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(jobService.startJob(companyId));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<CashIntelligenceJobDTO> getJob(@PathVariable String jobId) {
        return jobService.getJob(jobId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/simulate")
    public ResponseEntity<ScenarioSimulationResultDTO> simulate(@RequestBody ScenarioSimulationRequestDTO request) {
        return ResponseEntity.ok(scenarioSimulationService.simulate(request));
    }
}
