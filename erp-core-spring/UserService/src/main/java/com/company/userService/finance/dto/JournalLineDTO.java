package com.company.userService.finance.dto;

import java.math.BigDecimal;

public class JournalLineDTO {
    private String id;
    private String account_code;
    private String account_name;
    private BigDecimal debit;
    private BigDecimal credit;

    public JournalLineDTO() {
    }

    public JournalLineDTO(String id,
                          String account_code,
                          String account_name,
                          BigDecimal debit,
                          BigDecimal credit) {
        this.id = id;
        this.account_code = account_code;
        this.account_name = account_name;
        this.debit = debit;
        this.credit = credit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }
}
