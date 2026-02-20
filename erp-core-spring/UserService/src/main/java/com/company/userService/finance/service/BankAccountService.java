package com.company.userService.finance.service;

import com.company.erp.erp.entites.finance.BankAccount;
import com.company.erp.erp.entites.finance.Company;
import com.company.userService.finance.mapper.BankAccountMapper;
import com.company.userService.finance.dto.BankAccountCreateRequest;
import com.company.userService.finance.dto.BankAccountResponse;
import com.company.userService.finance.repository.BankAccountRepository;
import com.company.userService.finance.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CompanyRepository companyRepository;

    public BankAccountResponse createAccount(BankAccountCreateRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        BankAccount account = BankAccountMapper.INSTANCE.toEntity(request);
        account.setCompany(company);
        BankAccount saved = bankAccountRepository.save(account);
        return BankAccountMapper.INSTANCE.toResponse(saved);
    }

    public BankAccountResponse getAccountById(Long id) {
        BankAccount account = bankAccountRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bank account not found with id: " + id));
        return BankAccountMapper.INSTANCE.toResponse(account);
    }

    public BankAccountResponse getAccountByNumber(String accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> 
            new RuntimeException("Bank account not found with number: " + accountNumber));
        return BankAccountMapper.INSTANCE.toResponse(account);
    }

    public List<BankAccountResponse> getAccountsByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return bankAccountRepository.findByCompany(company).stream()
            .map(BankAccountMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public List<BankAccountResponse> getActiveAccountsByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        return bankAccountRepository.findByCompanyAndIsActive(company, true).stream()
            .map(BankAccountMapper.INSTANCE::toResponse)
            .collect(Collectors.toList());
    }

    public void depositFunds(Long id, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bank account not found with id: " + id));
        account.setBalance(account.getBalance().add(amount));
        bankAccountRepository.save(account);
    }

    public void withdrawFunds(Long id, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bank account not found with id: " + id));
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in account");
        }
        
        account.setBalance(account.getBalance().subtract(amount));
        bankAccountRepository.save(account);
    }

    public BankAccountResponse updateAccount(Long id, BankAccountCreateRequest request) {
        BankAccount account = bankAccountRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Bank account not found with id: " + id));
        
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountName(request.getAccountName());
        account.setBankName(request.getBankName());
        account.setAccountType(request.getAccountType());
        account.setIsActive(request.getIsActive());
        
        BankAccount saved = bankAccountRepository.save(account);
        return BankAccountMapper.INSTANCE.toResponse(saved);
    }

    public void deleteAccount(Long id) {
        bankAccountRepository.deleteById(id);
    }
}
