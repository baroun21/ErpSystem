package com.company.erp.shared.infrastructure.event.listener;

import com.company.erp.cashintelligence.service.ReceivableRiskService;
import com.company.erp.finance.domain.event.BillPostedEvent;
import com.company.erp.finance.domain.event.InvoicePostedEvent;
import com.company.erp.finance.domain.event.PaymentReceivedEvent;
import com.company.erp.shared.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskAnalyzer {

    private final ReceivableRiskService receivableRiskService;

    @Async
    @EventListener
    public void onInvoicePosted(InvoicePostedEvent event) {
        evaluateRisk(event, "InvoicePostedEvent");
    }

    @Async
    @EventListener
    public void onPaymentReceived(PaymentReceivedEvent event) {
        evaluateRisk(event, "PaymentReceivedEvent");
    }

    @Async
    @EventListener
    public void onBillPosted(BillPostedEvent event) {
        evaluateRisk(event, "BillPostedEvent");
    }

    private void evaluateRisk(DomainEvent event, String source) {
        if (event.getTenantId() == null) {
            log.debug("RiskAnalyzer skipped: missing tenantId for {}", source);
            return;
        }
        var risk = receivableRiskService.buildReceivableRisk(event.getTenantId());
        log.info("RiskAnalyzer updated: top customer {} ({}%)", risk.getTopCustomerName(), risk.getTopCustomerShare());
    }
}
