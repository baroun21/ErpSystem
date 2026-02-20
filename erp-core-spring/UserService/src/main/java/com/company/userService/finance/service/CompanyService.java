package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.Company;
import com.company.userService.finance.mapper.CompanyMapper;
import com.company.userService.finance.dto.CompanyCreateRequest;
import com.company.userService.finance.dto.CompanyResponse;
import com.company.userService.finance.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyResponse createCompany(CompanyCreateRequest request) {
        Company company = CompanyMapper.INSTANCE.toEntity(request);
        Company saved = companyRepository.save(company);
        return CompanyMapper.INSTANCE.toResponse(saved);
    }

    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Company not found with id: " + id));
        return CompanyMapper.INSTANCE.toResponse(company);
    }

    public CompanyResponse getCompanyByCode(String code) {
        Company company = companyRepository.findByCode(code).orElseThrow(() -> 
            new RuntimeException("Company not found with code: " + code));
        return CompanyMapper.INSTANCE.toResponse(company);
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
            .map(CompanyMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public CompanyResponse updateCompany(Long id, CompanyCreateRequest request) {
        Company company = companyRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Company not found with id: " + id));
        
        company.setCode(request.getCode());
        company.setName(request.getName());
        company.setStreet(request.getStreet());
        company.setCity(request.getCity());
        company.setState(request.getState());
        company.setPostalCode(request.getPostalCode());
        company.setCountry(request.getCountry());
        company.setPhone(request.getPhone());
        company.setEmail(request.getEmail());
        company.setTaxId(request.getTaxId());
        
        Company saved = companyRepository.save(company);
        return CompanyMapper.INSTANCE.toResponse(saved);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
