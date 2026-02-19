package com.company.userService.finance.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Example event listeners for Finance events
 * Other modules can implement similar listeners
 */
@Service
public class FinanceEventListeners {

    private static final Logger logger = LoggerFactory.getLogger(FinanceEventListeners.class);
    
    /**
     * When an invoice is posted, perform related HR/business operations
     */
    @EventListener
    public void onInvoicePosted(InvoicePostedEvent event) {
        logger.info("Processing InvoicePostedEvent: {} for customer: {}", 
            event.getInvoiceNumber(), event.getCustomerId());
        
        // Example: Update customer credit info
        // Example: Create accounting entries in HR system
        // Example: Update sales dashboards
        // Example: Trigger notifications
    }
    
    /**
     * When a journal entry is posted, propagate to other modules
     */
    @EventListener
    public void onJournalEntryPosted(JournalEntryPostedEvent event) {
        logger.info("Processing JournalEntryPostedEvent: {} for company: {}", 
            event.getReference(), event.getCompanyId());
        
        // Example: Update GL balances
        // Example: Trigger compliance checks
        // Example: Update financial dashboards
    }
    
    /**
     * When payment is received, update AR and employee records
     */
    @EventListener
    public void onPaymentReceived(PaymentReceivedEvent event) {
        logger.info("Processing PaymentReceivedEvent: {} for customer: {}", 
            event.getPaymentId(), event.getCustomerId());
        
        // Example: Update invoice status
        // Example: Create revenue recognition entry
        // Example: Update customer AR balance
        // Example: Trigger commission calculations if applicable
    }
    
    /**
     * When a bill is posted, notify AP and expense tracking
     */
    @EventListener
    public void onBillPosted(BillPostedEvent event) {
        logger.info("Processing BillPostedEvent: {} from supplier: {}", 
            event.getBillNumber(), event.getSupplierId());
        
        // Example: Create expense entries
        // Example: Update budget tracking
        // Example: Notify procurement
        // Example: Trigger payment scheduling
    }
}
