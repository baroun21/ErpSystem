package com.company.erp.shared.context;

/**
 * Thread-local context for managing current tenant (company) during request processing.
 * Ensures all database queries are automatically filtered by company_id.
 * Must be cleared in a finally block or via interceptors.
 */
public class TenantContext {
    private static final ThreadLocal<Long> tenantHolder = new ThreadLocal<>();
    private static final ThreadLocal<Long> legalEntityHolder = new ThreadLocal<>();

    /**
     * Set current tenant ID (company_id)
     */
    public static void setCurrentTenant(Long tenantId) {
        tenantHolder.set(tenantId);
    }

    /**
     * Get current tenant ID
     */
    public static Long getCurrentTenant() {
        Long tenantId = tenantHolder.get();
        if (tenantId == null) {
            throw new IllegalStateException("No tenant context set. Check TenantInterceptor is registered.");
        }
        return tenantId;
    }

    /**
     * Set legal entity (optional, for multi-entity support)
     */
    public static void setCurrentLegalEntity(Long legalEntityId) {
        legalEntityHolder.set(legalEntityId);
    }

    /**
     * Get legal entity if set
     */
    public static Long getCurrentLegalEntity() {
        return legalEntityHolder.get();
    }

    /**
     * Clear all tenant context (must call in finally block)
     */
    public static void clear() {
        tenantHolder.remove();
        legalEntityHolder.remove();
    }

    /**
     * Check if tenant context is set
     */
    public static boolean hasTenant() {
        return tenantHolder.get() != null;
    }
}

