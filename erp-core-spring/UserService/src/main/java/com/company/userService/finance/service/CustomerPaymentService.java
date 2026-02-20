package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.CustomerPayment;
import com.company.erp.erp.entites.finance.Company;
import com.company.erp.erp.entites.finance.Customer;
import com.company.userService.finance.mapper.CustomerPaymentMapper;
import com.company.userService.finance.dto.CustomerPaymentCreateRequest;
import com.company.userService.finance.dto.CustomerPaymentResponse;
import com.company.userService.finance.repository.CustomerPaymentRepository;
import com.company.userService.finance.repository.CompanyRepository;
import com.company.userService.finance.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerPaymentService {
    private final CustomerPaymentRepository customerPaymentRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final InvoiceService invoiceService;

    public CustomerPaymentResponse recordPayment(CustomerPaymentCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        CustomerPayment payment = CustomerPaymentMapper.INSTANCE.toEntity(request);
        payment.setCompany(company);
        payment.setCustomer(customer);
        CustomerPayment saved = customerPaymentRepository.save(payment);
        
        // Update invoice paid amount if linked
        if (request.getInvoiceId() != null) {
            invoiceService.recordPayment(request.getInvoiceId(), request.getPaymentAmount());
        }
        
        return CustomerPaymentMapper.INSTANCE.toResponse(saved);
    }

    public CustomerPaymentResponse getPaymentById(Long id) {
        CustomerPayment payment = customerPaymentRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Payment not found with id: " + id));
        return CustomerPaymentMapper.INSTANCE.toResponse(payment);
    }

    public List<CustomerPaymentResponse> getPaymentsByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return customerPaymentRepository.findByCompany(company).stream()
            .map(CustomerPaymentMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<CustomerPaymentResponse> getPaymentsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerPaymentRepository.findByCustomer(customer).stream()
            .map(CustomerPaymentMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<CustomerPaymentResponse> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return customerPaymentRepository.findByPaymentDateBetween(startDate, endDate).stream()
            .map(CustomerPaymentMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public void deletePayment(Long id) {
        customerPaymentRepository.deleteById(id);
    }
}
