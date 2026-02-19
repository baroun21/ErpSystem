package com.company.userService.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TrialBalanceDTO {
    private String id;
    private String company_id;
    private LocalDate as_of_date;
    private String account_code;
    private String account_name;
    private BigDecimal debit_balance;
    private BigDecimal credit_balance;

    public TrialBalanceDTO() {
    }

    public TrialBalanceDTO(String id,
                           String company_id,
                           LocalDate as_of_date,
                           String account_code,
                           String account_name,
                           BigDecimal debit_balance,
                           BigDecimal credit_balance) {
        this.id = id;
        this.company_id = company_id;
        this.as_of_date = as_of_date;
        this.account_code = account_code;
        this.account_name = account_name;
        this.debit_balance = debit_balance;
        this.credit_balance = credit_balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public LocalDate getAs_of_date() {
        return as_of_date;
    }

    public void setAs_of_date(LocalDate as_of_date) {
        this.as_of_date = as_of_date;
    }

    public String getAccount_code() {
        return account_code;
    }

    public void setAccount_code(String account_code) {
        this.account_code = account_code;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public BigDecimal getDebit_balance() {
        return debit_balance;
    }

    public void setDebit_balance(BigDecimal debit_balance) {
        this.debit_balance = debit_balance;
    }

    public BigDecimal getCredit_balance() {
        return credit_balance;
    }

    public void setCredit_balance(BigDecimal credit_balance) {
        this.credit_balance = credit_balance;
    }
}
