package com.company.userService.finance.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Publishes finance events to other services
 */
@Service
public class FinanceEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(FinanceEventPublisher.class);
    
    private final ApplicationEventPublisher eventPublisher;

    public FinanceEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public void publishInvoicePosted(InvoicePostedEvent event) {
        logger.info("Publishing InvoicePostedEvent: {}", event.getInvoiceNumber());
        eventPublisher.publishEvent(event);
    }
    
    public void publishJournalEntryPosted(JournalEntryPostedEvent event) {
        logger.info("Publishing JournalEntryPostedEvent: {}", event.getReference());
        eventPublisher.publishEvent(event);
    }
    
    public void publishPaymentReceived(PaymentReceivedEvent event) {
        logger.info("Publishing PaymentReceivedEvent: {}", event.getPaymentId());
        eventPublisher.publishEvent(event);
    }
    
    public void publishBillPosted(BillPostedEvent event) {
        logger.info("Publishing BillPostedEvent: {}", event.getBillNumber());
        eventPublisher.publishEvent(event);
    }
}
