package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.Customer;
import com.company.erp.erp.entites.finance.Company;
import com.company.userService.finance.mapper.CustomerMapper;
import com.company.userService.finance.dto.CustomerCreateRequest;
import com.company.userService.finance.dto.CustomerResponse;
import com.company.userService.finance.repository.CustomerRepository;
import com.company.userService.finance.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public CustomerResponse createCustomer(CustomerCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        Customer customer = CustomerMapper.INSTANCE.toEntity(request);
        customer.setCompany(company);
        Customer saved = customerRepository.save(customer);
        return CustomerMapper.INSTANCE.toResponse(saved);
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Customer not found with id: " + id));
        return CustomerMapper.INSTANCE.toResponse(customer);
    }

    public List<CustomerResponse> getCustomersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return customerRepository.findByCompany(company).stream()
            .map(CustomerMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<CustomerResponse> getActiveCustomersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return customerRepository.findByCompanyAndIsActive(company, true).stream()
            .map(CustomerMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public CustomerResponse updateCustomer(Long id, CustomerCreateRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Customer not found with id: " + id));
        
        customer.setCode(request.getCode());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setStreet(request.getStreet());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setPostalCode(request.getPostalCode());
        customer.setCountry(request.getCountry());
        customer.setCreditLimit(request.getCreditLimit());
        customer.setIsActive(request.getIsActive());
        
        Customer saved = customerRepository.save(customer);
        return CustomerMapper.INSTANCE.toResponse(saved);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
