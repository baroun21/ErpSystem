package com.company.erp.erp.entites;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class TenantEntityListener {

    @PrePersist
    public void prePersist(Object entity) {
        applyTenant(entity);
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        applyTenant(entity);
    }

    private void applyTenant(Object entity) {
        if (!(entity instanceof BaseEntity baseEntity)) {
            return;
        }

        // 1) لو معموله set يدوي (bootstrap) سيبه
        if (baseEntity.getCompanyId() != null) {
            return;
        }

        // 2) otherwise خد من context (طلبات بعد JWT)
        Long companyId = TenantContext.getCompanyId();
        if (companyId == null) {
            throw new RuntimeException(
                    "Multi-tenancy Error: Company ID not found. " +
                            "Set entity.companyId explicitly (bootstrap) or ensure JWT filter sets TenantContext."
            );
        }

        baseEntity.setCompanyId(companyId);
    }
}