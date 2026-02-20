package com.company.erp.shared.infrastructure.event.listener;

import com.company.erp.finance.domain.event.BillPostedEvent;
import com.company.erp.finance.domain.event.InvoicePostedEvent;
import com.company.erp.finance.domain.event.JournalEntryPostedEvent;
import com.company.erp.finance.domain.event.PaymentReceivedEvent;
import com.company.erp.hr.domain.event.PayrollPostedEvent;
import com.company.erp.inventory.domain.event.StockAdjustedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardUpdater {

    @Async
    @EventListener
    public void onInvoicePosted(InvoicePostedEvent event) {
        log.info("DashboardUpdater received InvoicePostedEvent for invoice {}", event.getInvoiceNumber());
    }

    @Async
    @EventListener
    public void onPaymentReceived(PaymentReceivedEvent event) {
        log.info("DashboardUpdater received PaymentReceivedEvent for payment {}", event.getPaymentNumber());
    }

    @Async
    @EventListener
    public void onBillPosted(BillPostedEvent event) {
        log.info("DashboardUpdater received BillPostedEvent for bill {}", event.getBillNumber());
    }

    @Async
    @EventListener
    public void onJournalPosted(JournalEntryPostedEvent event) {
        log.info("DashboardUpdater received JournalEntryPostedEvent for reference {}", event.getReference());
    }

    @Async
    @EventListener
    public void onStockAdjusted(StockAdjustedEvent event) {
        log.info("DashboardUpdater received StockAdjustedEvent for product {}", event.getProductId());
    }

    @Async
    @EventListener
    public void onPayrollPosted(PayrollPostedEvent event) {
        log.info("DashboardUpdater received PayrollPostedEvent for payroll {}", event.getAggregateId());
    }
}
