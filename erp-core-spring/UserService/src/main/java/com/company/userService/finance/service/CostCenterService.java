package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.CostCenter;
import com.company.erp.erp.entites.finance.Company;
import com.company.userService.finance.mapper.CostCenterMapper;
import com.company.userService.finance.dto.CostCenterCreateRequest;
import com.company.userService.finance.dto.CostCenterResponse;
import com.company.userService.finance.repository.CostCenterRepository;
import com.company.userService.finance.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CostCenterService {
    private final CostCenterRepository costCenterRepository;
    private final CompanyRepository companyRepository;

    public CostCenterResponse createCostCenter(CostCenterCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        CostCenter costCenter = CostCenterMapper.INSTANCE.toEntity(request);
        costCenter.setCompany(company);
        CostCenter saved = costCenterRepository.save(costCenter);
        return CostCenterMapper.INSTANCE.toResponse(saved);
    }

    public CostCenterResponse getCostCenterById(Long id) {
        CostCenter costCenter = costCenterRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Cost center not found with id: " + id));
        return CostCenterMapper.INSTANCE.toResponse(costCenter);
    }

    public List<CostCenterResponse> getCostCentersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return costCenterRepository.findByCompany(company).stream()
            .map(CostCenterMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<CostCenterResponse> getActiveCostCentersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return costCenterRepository.findByCompanyAndIsActive(company, true).stream()
            .map(CostCenterMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public CostCenterResponse updateCostCenter(Long id, CostCenterCreateRequest request) {
        CostCenter costCenter = costCenterRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Cost center not found with id: " + id));
        
        costCenter.setCode(request.getCode());
        costCenter.setName(request.getName());
        costCenter.setDescription(request.getDescription());
        costCenter.setIsActive(request.getIsActive());
        
        CostCenter saved = costCenterRepository.save(costCenter);
        return CostCenterMapper.INSTANCE.toResponse(saved);
    }

    public void deleteCostCenter(Long id) {
        costCenterRepository.deleteById(id);
    }
}
