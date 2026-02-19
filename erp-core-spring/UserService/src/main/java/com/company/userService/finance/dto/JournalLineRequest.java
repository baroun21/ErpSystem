package com.company.userService.finance.dto;

import java.math.BigDecimal;

public class JournalLineRequest {
    private String account_id;
    private BigDecimal debit;
    private BigDecimal credit;
    private String cost_center_id;

    public JournalLineRequest() {
    }

    public JournalLineRequest(String account_id,
                              BigDecimal debit,
                              BigDecimal credit,
                              String cost_center_id) {
        this.account_id = account_id;
        this.debit = debit;
        this.credit = credit;
        this.cost_center_id = cost_center_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
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

    public String getCost_center_id() {
        return cost_center_id;
    }

    public void setCost_center_id(String cost_center_id) {
        this.cost_center_id = cost_center_id;
    }
}
