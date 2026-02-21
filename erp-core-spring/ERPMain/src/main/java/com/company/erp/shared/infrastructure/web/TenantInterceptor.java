package com.company.erp.shared.infrastructure.web;

import com.company.erp.shared.context.TenantContext;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Intercepts all HTTP requests to extract tenant (company_id) from JWT token
 * and set it in TenantContext for automatic multi-tenant filtering.
 *
 * Flow:
 *   1. Extract JWT from Authorization header
 *   2. Parse JWT to get tenantId claim
 *   3. Set TenantContext.setCurrentTenant(tenantId)
 *   4. After request: TenantContext.clear()
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret:mySecretKey}")
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                Long tenantId = extractTenantFromJWT(token);

                if (tenantId != null) {
                    TenantContext.setCurrentTenant(tenantId);
                    log.debug("Tenant context set for tenant: {}", tenantId);
                    return true;
                }
            }

            log.warn("No valid JWT token found in request to {}", request.getRequestURI());
            return true;  // Allow public endpoints

        } catch (Exception e) {
            log.warn("Error extracting tenant from JWT", e);
            return true;  // Allow request to proceed, let controller handle auth
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        // Always clear tenant context after request
        TenantContext.clear();
    }

    /**
     * Extract tenantId from JWT claims
     */
    private Long extractTenantFromJWT(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Object tenantIdObj = claims.get("tenantId");
            if (tenantIdObj instanceof Integer) {
                return ((Integer) tenantIdObj).longValue();
            } else if (tenantIdObj instanceof Long) {
                return (Long) tenantIdObj;
            }

            return null;
        } catch (Exception e) {
            log.debug("Failed to extract tenantId from JWT", e);
            return null;
        }
    }
}

