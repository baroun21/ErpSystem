package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.Invoice;
import com.company.erp.erp.entites.finance.Company;
import com.company.erp.erp.entites.finance.Customer;
import com.company.userService.finance.mapper.InvoiceMapper;
import com.company.userService.finance.dto.InvoiceCreateRequest;
import com.company.userService.finance.dto.InvoiceResponse;
import com.company.userService.finance.repository.InvoiceRepository;
import com.company.userService.finance.repository.CompanyRepository;
import com.company.userService.finance.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
            .map(InvoiceMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public InvoiceResponse createInvoice(InvoiceCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Invoice invoice = InvoiceMapper.INSTANCE.toEntity(request);
        invoice.setCompany(company);
        invoice.setCustomer(customer);
        invoice.setStatus("DRAFT");
        invoice.setPaidAmount(java.math.BigDecimal.ZERO);
        
        Invoice saved = invoiceRepository.save(invoice);
        return InvoiceMapper.INSTANCE.toResponse(saved);
    }

    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Invoice not found with id: " + id));
        return InvoiceMapper.INSTANCE.toResponse(invoice);
    }

    public List<InvoiceResponse> getInvoicesByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return invoiceRepository.findByCompany(company).stream()
            .map(InvoiceMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<InvoiceResponse> getInvoicesByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        return invoiceRepository.findByCustomer(customer).stream()
            .map(InvoiceMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public void postInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Invoice not found with id: " + id));
        invoice.setStatus("POSTED");
        invoiceRepository.save(invoice);
    }

    public void recordPayment(Long id, java.math.BigDecimal amount) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Invoice not found with id: " + id));
        
        java.math.BigDecimal newPaidAmount = invoice.getPaidAmount().add(amount);
        invoice.setPaidAmount(newPaidAmount);
        
        if (newPaidAmount.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus("PAID");
        } else {
            invoice.setStatus("PARTIAL");
        }
        
        invoiceRepository.save(invoice);
    }

    public InvoiceResponse updateInvoice(Long id, InvoiceCreateRequest request) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Invoice not found with id: " + id));
        
        if (!"DRAFT".equals(invoice.getStatus())) {
            throw new RuntimeException("Cannot update non-draft invoice");
        }
        
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setSubtotal(request.getSubtotal());
        invoice.setTaxAmount(request.getTaxAmount());
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setNotes(request.getNotes());
        
        Invoice saved = invoiceRepository.save(invoice);
        return InvoiceMapper.INSTANCE.toResponse(saved);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}
