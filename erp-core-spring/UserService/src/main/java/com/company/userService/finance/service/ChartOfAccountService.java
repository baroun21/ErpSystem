package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.ChartOfAccount;
import com.company.erp.erp.entites.finance.Company;
import com.company.userService.finance.mapper.ChartOfAccountMapper;
import com.company.userService.finance.dto.ChartOfAccountCreateRequest;
import com.company.userService.finance.dto.ChartOfAccountResponse;
import com.company.userService.finance.repository.ChartOfAccountRepository;
import com.company.userService.finance.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChartOfAccountService {
    private final ChartOfAccountRepository chartOfAccountRepository;
    private final CompanyRepository companyRepository;

    public ChartOfAccountResponse createAccount(ChartOfAccountCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        ChartOfAccount account = ChartOfAccountMapper.INSTANCE.toEntity(request);
        account.setCompany(company);
        ChartOfAccount saved = chartOfAccountRepository.save(account);
        return ChartOfAccountMapper.INSTANCE.toResponse(saved);
    }

    public ChartOfAccountResponse getAccountById(Long id) {
        ChartOfAccount account = chartOfAccountRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Account not found with id: " + id));
        return ChartOfAccountMapper.INSTANCE.toResponse(account);
    }

    public ChartOfAccountResponse getAccountByCode(String code) {
        ChartOfAccount account = chartOfAccountRepository.findByAccountCode(code).orElseThrow(() -> 
            new RuntimeException("Account not found with code: " + code));
        return ChartOfAccountMapper.INSTANCE.toResponse(account);
    }

    public List<ChartOfAccountResponse> getAccountsByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return chartOfAccountRepository.findByCompany(company).stream()
            .map(ChartOfAccountMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<ChartOfAccountResponse> getAccountsByType(Long companyId, String accountType) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return chartOfAccountRepository.findByCompanyAndAccountType(company, accountType).stream()
            .map(ChartOfAccountMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public ChartOfAccountResponse updateAccount(Long id, ChartOfAccountCreateRequest request) {
        ChartOfAccount account = chartOfAccountRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Account not found with id: " + id));
        
        account.setAccountCode(request.getAccountCode());
        account.setAccountName(request.getAccountName());
        account.setAccountType(request.getAccountType());
        account.setAccountSubType(request.getAccountSubType());
        account.setDescription(request.getDescription());
        account.setIsActive(request.getIsActive());
        
        ChartOfAccount saved = chartOfAccountRepository.save(account);
        return ChartOfAccountMapper.INSTANCE.toResponse(saved);
    }

    public void deleteAccount(Long id) {
        chartOfAccountRepository.deleteById(id);
    }
}
