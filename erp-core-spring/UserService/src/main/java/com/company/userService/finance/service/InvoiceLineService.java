package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.InvoiceLine;
import com.company.erp.erp.entites.finance.Invoice;
import com.company.userService.finance.mapper.InvoiceLineMapper;
import com.company.userService.finance.dto.InvoiceLineCreateRequest;
import com.company.userService.finance.dto.InvoiceLineResponse;
import com.company.userService.finance.repository.InvoiceLineRepository;
import com.company.userService.finance.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvoiceLineService {
    private final InvoiceLineRepository invoiceLineRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceLineResponse createInvoiceLine(InvoiceLineCreateRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
            .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        InvoiceLine line = InvoiceLineMapper.INSTANCE.toEntity(request);
        line.setInvoice(invoice);
        InvoiceLine saved = invoiceLineRepository.save(line);
        return InvoiceLineMapper.INSTANCE.toResponse(saved);
    }

    public InvoiceLineResponse getInvoiceLineById(Long id) {
        InvoiceLine line = invoiceLineRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Invoice line not found with id: " + id));
        return InvoiceLineMapper.INSTANCE.toResponse(line);
    }

    public List<InvoiceLineResponse> getInvoiceLinesByInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return invoiceLineRepository.findByInvoice(invoice).stream()
            .map(InvoiceLineMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public InvoiceLineResponse updateInvoiceLine(Long id, InvoiceLineCreateRequest request) {
        InvoiceLine line = invoiceLineRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Invoice line not found with id: " + id));
        
        line.setDescription(request.getDescription());
        line.setQuantity(request.getQuantity());
        line.setUnitPrice(request.getUnitPrice());
        line.setTaxAmount(request.getTaxAmount());
        line.setLineTotal(request.getLineTotal());
        
        InvoiceLine saved = invoiceLineRepository.save(line);
        return InvoiceLineMapper.INSTANCE.toResponse(saved);
    }

    public void deleteInvoiceLine(Long id) {
        invoiceLineRepository.deleteById(id);
    }
}
