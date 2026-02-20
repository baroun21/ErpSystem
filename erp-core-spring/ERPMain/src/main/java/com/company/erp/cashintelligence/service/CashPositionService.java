package com.company.erp.cashintelligence.service;

import com.company.erp.erp.Dtos.cash.CashPositionDTO;
import com.company.erp.finance.domain.entity.BankAccount;
import com.company.erp.finance.domain.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CashPositionService {

    private final BankAccountRepository bankAccountRepository;

    @Transactional(readOnly = true)
    public CashPositionDTO getCashPosition(Long companyId) {
        List<BankAccount> accounts = bankAccountRepository.findAllByCompanyId(companyId);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal reconciled = BigDecimal.ZERO;
        Map<String, BigDecimal> byCurrency = new HashMap<>();

        for (BankAccount account : accounts) {
            BigDecimal balance = safe(account.getCurrentBalance());
            BigDecimal reconciledBalance = safe(account.getReconciledBalance());
            total = total.add(balance);
            reconciled = reconciled.add(reconciledBalance);

            String currency = account.getCurrency() == null ? "USD" : account.getCurrency();
            byCurrency.put(currency, byCurrency.getOrDefault(currency, BigDecimal.ZERO).add(balance));
        }

        return CashPositionDTO.builder()
            .totalCash(total)
            .reconciledCash(reconciled)
            .accountCount(accounts.size())
            .balancesByCurrency(byCurrency)
            .build();
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
