package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.SupplierPayment;
import com.company.erp.erp.entites.finance.Company;
import com.company.erp.erp.entites.finance.Supplier;
import com.company.userService.finance.mapper.SupplierPaymentMapper;
import com.company.userService.finance.dto.SupplierPaymentCreateRequest;
import com.company.userService.finance.dto.SupplierPaymentResponse;
import com.company.userService.finance.repository.SupplierPaymentRepository;
import com.company.userService.finance.repository.CompanyRepository;
import com.company.userService.finance.repository.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupplierPaymentService {
    private final SupplierPaymentRepository supplierPaymentRepository;
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;
    private final BillService billService;

    public SupplierPaymentResponse recordPayment(SupplierPaymentCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        SupplierPayment payment = SupplierPaymentMapper.INSTANCE.toEntity(request);
        payment.setCompany(company);
        payment.setSupplier(supplier);
        SupplierPayment saved = supplierPaymentRepository.save(payment);
        
        // Update bill paid amount if linked
        if (request.getBillId() != null) {
            billService.recordPayment(request.getBillId(), request.getPaymentAmount());
        }
        
        return SupplierPaymentMapper.INSTANCE.toResponse(saved);
    }

    public SupplierPaymentResponse getPaymentById(Long id) {
        SupplierPayment payment = supplierPaymentRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Payment not found with id: " + id));
        return SupplierPaymentMapper.INSTANCE.toResponse(payment);
    }

    public List<SupplierPaymentResponse> getPaymentsByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return supplierPaymentRepository.findByCompany(company).stream()
            .map(SupplierPaymentMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<SupplierPaymentResponse> getPaymentsBySupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return supplierPaymentRepository.findBySupplier(supplier).stream()
            .map(SupplierPaymentMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<SupplierPaymentResponse> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return supplierPaymentRepository.findByPaymentDateBetween(startDate, endDate).stream()
            .map(SupplierPaymentMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public void deletePayment(Long id) {
        supplierPaymentRepository.deleteById(id);
    }
}
