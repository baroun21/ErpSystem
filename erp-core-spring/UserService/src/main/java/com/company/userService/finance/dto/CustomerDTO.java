package com.company.userService.finance.dto;

import java.math.BigDecimal;

public class CustomerDTO {
    private String id;
    private String company_id;
    private String name;
    private String tax_id;
    private String email;
    private String phone;
    private String payment_terms;
    private BigDecimal credit_limit;
    private Boolean is_active;
    private String ar_balance;

    public CustomerDTO() {
    }

    public CustomerDTO(String id,
                       String company_id,
                       String name,
                       String tax_id,
                       String email,
                       String phone,
                       String payment_terms,
                       BigDecimal credit_limit,
                       Boolean is_active,
                       String ar_balance) {
        this.id = id;
        this.company_id = company_id;
        this.name = name;
        this.tax_id = tax_id;
        this.email = email;
        this.phone = phone;
        this.payment_terms = payment_terms;
        this.credit_limit = credit_limit;
        this.is_active = is_active;
        this.ar_balance = ar_balance;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(String payment_terms) {
        this.payment_terms = payment_terms;
    }

    public BigDecimal getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(BigDecimal credit_limit) {
        this.credit_limit = credit_limit;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getAr_balance() {
        return ar_balance;
    }

    public void setAr_balance(String ar_balance) {
        this.ar_balance = ar_balance;
    }
}
