package com.company.userService.finance.client;

import com.company.userService.finance.config.FinanceServiceProperties;
import com.company.userService.finance.dto.*;
import com.company.userService.finance.exception.FinanceServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * REST Client for internal finance service calls (within Spring Boot)
 * Handles communication between modules for cross-service integration
 * Note: Finance module is integrated in Spring Boot; this client is for module-to-module calls if needed
 */
@Service
public class FinanceServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(FinanceServiceClient.class);
    
    private final RestTemplate restTemplate;
    private final FinanceServiceProperties properties;

    public FinanceServiceClient(RestTemplate restTemplate, FinanceServiceProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }
    
    /**
     * Create a new journal entry
     */
    public JournalEntryDTO createJournalEntry(CreateJournalEntryRequest request) {
        try {
            String url = properties.getBaseUrl() + "/journal-entries/";
            
            HttpEntity<CreateJournalEntryRequest> entity = new HttpEntity<>(request, getHeaders());
            
            ResponseEntity<JournalEntryDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                JournalEntryDTO.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Journal entry created: {}", response.getBody().getReference());
                return response.getBody();
            } else {
                throw new FinanceServiceException("Failed to create journal entry: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error creating journal entry", e);
            throw new FinanceServiceException("Finance service error", e);
        }
    }
    
    /**
     * Post a journal entry (make it immutable)
     */
    public JournalEntryDTO postJournalEntry(String journalEntryId) {
        try {
            String url = properties.getBaseUrl() + "/journal-entries/" + journalEntryId + "/post_entry/";
            
            HttpEntity<?> entity = new HttpEntity<>(getHeaders());
            
            ResponseEntity<JournalEntryDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                JournalEntryDTO.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Journal entry posted: {}", journalEntryId);
                return response.getBody();
            } else {
                throw new FinanceServiceException("Failed to post journal entry: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error posting journal entry", e);
            throw new FinanceServiceException("Finance service error", e);
        }
    }
    
    /**
     * Create a customer
     */
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        try {
            String url = properties.getBaseUrl() + "/customers/";
            
            HttpEntity<CreateCustomerRequest> entity = new HttpEntity<>(request, getHeaders());
            
            ResponseEntity<CustomerDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                CustomerDTO.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Customer created: {}", response.getBody().getName());
                return response.getBody();
            } else {
                throw new FinanceServiceException("Failed to create customer: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error creating customer", e);
            throw new FinanceServiceException("Finance service error", e);
        }
    }
    
    /**
     * Create an invoice
     */
    public InvoiceDTO createInvoice(CreateInvoiceRequest request) {
        try {
            String url = properties.getBaseUrl() + "/invoices/";
            
            HttpEntity<CreateInvoiceRequest> entity = new HttpEntity<>(request, getHeaders());
            
            ResponseEntity<InvoiceDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                InvoiceDTO.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Invoice created: {}", response.getBody().getInvoiceNumber());
                return response.getBody();
            } else {
                throw new FinanceServiceException("Failed to create invoice: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error creating invoice", e);
            throw new FinanceServiceException("Finance service error", e);
        }
    }
    
    /**
     * Post an invoice
     */
    public InvoiceDTO postInvoice(String invoiceId) {
        try {
            String url = properties.getBaseUrl() + "/invoices/" + invoiceId + "/post_invoice/";
            
            HttpEntity<?> entity = new HttpEntity<>(getHeaders());
            
            ResponseEntity<InvoiceDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                InvoiceDTO.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Invoice posted: {}", invoiceId);
                return response.getBody();
            } else {
                throw new FinanceServiceException("Failed to post invoice: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error posting invoice", e);
            throw new FinanceServiceException("Finance service error", e);
        }
    }
    
    /**
     * Get trial balance
     */
    public List<TrialBalanceDTO> getTrialBalance(String companyId, String asOfDate) {
        try {
            String url = properties.getBaseUrl() + "/trial-balance/?company=" + companyId + "&as_of_date=" + asOfDate;
            
            HttpEntity<?> entity = new HttpEntity<>(getHeaders());
            
            ResponseEntity<TrialBalanceDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                TrialBalanceDTO[].class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Trial balance retrieved for company: {}", companyId);
                return Arrays.asList(response.getBody());
            } else {
                throw new FinanceServiceException("Failed to get trial balance: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error getting trial balance", e);
            throw new FinanceServiceException("Finance service error", e);
        }
    }
    
    /**
     * Get AR balance for customer
     */
    public String getARBalance(String customerId) {
        try {
            String url = properties.getBaseUrl() + "/customers/" + customerId + "/ar_balance/";
            
            HttpEntity<?> entity = new HttpEntity<>(getHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("AR balance retrieved for customer: {}", customerId);
                return response.getBody();
            } else {
                throw new FinanceServiceException("Failed to get AR balance: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error getting AR balance", e);
            throw new FinanceServiceException("Finance service error", e);
        }
    }
    
    /**
     * Check if Finance service is healthy
     */
    public boolean isHealthy() {
        try {
            String url = properties.getBaseUrl() + "/companies/";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.warn("Finance service health check failed", e);
            return false;
        }
    }
    
    /**
     * Build HTTP headers with authentication
     */
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        
        // Add API key if configured
        if (properties.getApiKey() != null && !properties.getApiKey().isEmpty()) {
            headers.set("Authorization", "Bearer " + properties.getApiKey());
        }
        
        return headers;
    }
}
