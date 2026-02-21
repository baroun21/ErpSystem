package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.Supplier;
import com.company.erp.erp.entites.finance.Company;
import com.company.userService.finance.mapper.SupplierMapper;
import com.company.userService.finance.dto.SupplierCreateRequest;
import com.company.userService.finance.dto.SupplierResponse;
import com.company.userService.finance.repository.SupplierRepository;
import com.company.userService.finance.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final CompanyRepository companyRepository;

    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
            .map(SupplierMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public SupplierResponse createSupplier(SupplierCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        Supplier supplier = SupplierMapper.INSTANCE.toEntity(request);
        supplier.setCompany(company);
        Supplier saved = supplierRepository.save(supplier);
        return SupplierMapper.INSTANCE.toResponse(saved);
    }

    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Supplier not found with id: " + id));
        return SupplierMapper.INSTANCE.toResponse(supplier);
    }

    public List<SupplierResponse> getSuppliersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return supplierRepository.findByCompany(company).stream()
            .map(SupplierMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<SupplierResponse> getActiveSuppliersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return supplierRepository.findByCompanyAndIsActive(company, true).stream()
            .map(SupplierMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public SupplierResponse updateSupplier(Long id, SupplierCreateRequest request) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Supplier not found with id: " + id));
        
        supplier.setCode(request.getCode());
        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setStreet(request.getStreet());
        supplier.setCity(request.getCity());
        supplier.setState(request.getState());
        supplier.setPostalCode(request.getPostalCode());
        supplier.setCountry(request.getCountry());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setIsActive(request.getIsActive());
        
        Supplier saved = supplierRepository.save(supplier);
        return SupplierMapper.INSTANCE.toResponse(saved);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
