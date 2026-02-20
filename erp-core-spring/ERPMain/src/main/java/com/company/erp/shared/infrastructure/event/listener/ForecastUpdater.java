package com.company.erp.shared.infrastructure.event.listener;

import com.company.erp.cashintelligence.service.CashIntelligenceJobService;
import com.company.erp.finance.domain.event.BillPostedEvent;
import com.company.erp.finance.domain.event.InvoicePostedEvent;
import com.company.erp.finance.domain.event.JournalEntryPostedEvent;
import com.company.erp.finance.domain.event.PaymentReceivedEvent;
import com.company.erp.hr.domain.event.PayrollPostedEvent;
import com.company.erp.inventory.domain.event.StockAdjustedEvent;
import com.company.erp.shared.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForecastUpdater {

    private final CashIntelligenceJobService jobService;

    @Async
    @EventListener
    public void onInvoicePosted(InvoicePostedEvent event) {
        startForecast(event, "InvoicePostedEvent");
    }

    @Async
    @EventListener
    public void onPaymentReceived(PaymentReceivedEvent event) {
        startForecast(event, "PaymentReceivedEvent");
    }

    @Async
    @EventListener
    public void onBillPosted(BillPostedEvent event) {
        startForecast(event, "BillPostedEvent");
    }

    @Async
    @EventListener
    public void onJournalPosted(JournalEntryPostedEvent event) {
        startForecast(event, "JournalEntryPostedEvent");
    }

    @Async
    @EventListener
    public void onStockAdjusted(StockAdjustedEvent event) {
        startForecast(event, "StockAdjustedEvent");
    }

    @Async
    @EventListener
    public void onPayrollPosted(PayrollPostedEvent event) {
        startForecast(event, "PayrollPostedEvent");
    }

    private void startForecast(DomainEvent event, String source) {
        if (event.getTenantId() == null) {
            log.debug("ForecastUpdater skipped: missing tenantId for {}", source);
            return;
        }
        jobService.startJob(event.getTenantId());
        log.info("ForecastUpdater triggered cash intelligence job from {}", source);
    }
}
