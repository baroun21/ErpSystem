package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.Bill;
import com.company.erp.erp.entites.finance.Company;
import com.company.erp.erp.entites.finance.Supplier;
import com.company.userService.finance.mapper.BillMapper;
import com.company.userService.finance.dto.BillCreateRequest;
import com.company.userService.finance.dto.BillResponse;
import com.company.userService.finance.repository.BillRepository;
import com.company.userService.finance.repository.CompanyRepository;
import com.company.userService.finance.repository.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;

    public BillResponse createBill(BillCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        Bill bill = BillMapper.INSTANCE.toEntity(request);
        bill.setCompany(company);
        bill.setSupplier(supplier);
        bill.setStatus("DRAFT");
        bill.setPaidAmount(java.math.BigDecimal.ZERO);
        
        Bill saved = billRepository.save(bill);
        return BillMapper.INSTANCE.toResponse(saved);
    }

    public BillResponse getBillById(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bill not found with id: " + id));
        return BillMapper.INSTANCE.toResponse(bill);
    }

    public List<BillResponse> getBillsByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return billRepository.findByCompany(company).stream()
            .map(BillMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<BillResponse> getBillsBySupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return billRepository.findBySupplier(supplier).stream()
            .map(BillMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public void postBill(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bill not found with id: " + id));
        bill.setStatus("POSTED");
        billRepository.save(bill);
    }

    public void recordPayment(Long id, java.math.BigDecimal amount) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bill not found with id: " + id));
        
        java.math.BigDecimal newPaidAmount = bill.getPaidAmount().add(amount);
        bill.setPaidAmount(newPaidAmount);
        
        if (newPaidAmount.compareTo(bill.getTotalAmount()) >= 0) {
            bill.setStatus("PAID");
        } else {
            bill.setStatus("PARTIAL");
        }
        
        billRepository.save(bill);
    }

    public BillResponse updateBill(Long id, BillCreateRequest request) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bill not found with id: " + id));
        
        if (!"DRAFT".equals(bill.getStatus())) {
            throw new RuntimeException("Cannot update non-draft bill");
        }
        
        bill.setBillNumber(request.getBillNumber());
        bill.setBillDate(request.getBillDate());
        bill.setDueDate(request.getDueDate());
        bill.setSubtotal(request.getSubtotal());
        bill.setTaxAmount(request.getTaxAmount());
        bill.setTotalAmount(request.getTotalAmount());
        bill.setNotes(request.getNotes());
        
        Bill saved = billRepository.save(bill);
        return BillMapper.INSTANCE.toResponse(saved);
    }

    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}
