package com.company.erp.erp.entites;


import lombok.Getter;

@Getter
public class TenantContext {
    // ThreadLocal ensures that different users' requests don't mix up company IDs
    private static final ThreadLocal<Long> currentCompanyId = new ThreadLocal<>();

    public static void setCompanyId(Long companyId) {
        currentCompanyId.set(companyId);
    }

    public static Long getCompanyId() {
        return currentCompanyId.get();
    }

    public static void clear() {
        currentCompanyId.remove();
    }
}