package com.company.userService.finance.dto;

import java.math.BigDecimal;

public class CreateCustomerRequest {
    private String company_id;
    private String name;
    private String tax_id;
    private String email;
    private String phone;
    private String payment_terms;
    private BigDecimal credit_limit;
    private String ar_account_id;

    public CreateCustomerRequest() {
    }

    public CreateCustomerRequest(String company_id,
                                 String name,
                                 String tax_id,
                                 String email,
                                 String phone,
                                 String payment_terms,
                                 BigDecimal credit_limit,
                                 String ar_account_id) {
        this.company_id = company_id;
        this.name = name;
        this.tax_id = tax_id;
        this.email = email;
        this.phone = phone;
        this.payment_terms = payment_terms;
        this.credit_limit = credit_limit;
        this.ar_account_id = ar_account_id;
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

    public String getAr_account_id() {
        return ar_account_id;
    }

    public void setAr_account_id(String ar_account_id) {
        this.ar_account_id = ar_account_id;
    }
}
