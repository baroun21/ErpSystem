package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.BillLine;
import com.company.erp.erp.entites.finance.Bill;
import com.company.userService.finance.mapper.BillLineMapper;
import com.company.userService.finance.dto.BillLineCreateRequest;
import com.company.userService.finance.dto.BillLineResponse;
import com.company.userService.finance.repository.BillLineRepository;
import com.company.userService.finance.repository.BillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BillLineService {
    private final BillLineRepository billLineRepository;
    private final BillRepository billRepository;

    public BillLineResponse createBillLine(BillLineCreateRequest request) {
        Bill bill = billRepository.findById(request.getBillId())
            .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        BillLine line = BillLineMapper.INSTANCE.toEntity(request);
        line.setBill(bill);
        BillLine saved = billLineRepository.save(line);
        return BillLineMapper.INSTANCE.toResponse(saved);
    }

    public BillLineResponse getBillLineById(Long id) {
        BillLine line = billLineRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bill line not found with id: " + id));
        return BillLineMapper.INSTANCE.toResponse(line);
    }

    public List<BillLineResponse> getBillLinesByBill(Long billId) {
        Bill bill = billRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found"));
        return billLineRepository.findByBill(bill).stream()
            .map(BillLineMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public BillLineResponse updateBillLine(Long id, BillLineCreateRequest request) {
        BillLine line = billLineRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bill line not found with id: " + id));
        
        line.setDescription(request.getDescription());
        line.setQuantity(request.getQuantity());
        line.setUnitPrice(request.getUnitPrice());
        line.setTaxAmount(request.getTaxAmount());
        line.setLineTotal(request.getLineTotal());
        
        BillLine saved = billLineRepository.save(line);
        return BillLineMapper.INSTANCE.toResponse(saved);
    }

    public void deleteBillLine(Long id) {
        billLineRepository.deleteById(id);
    }
}
