package com.company.userService.UserService.TenantUtils;

import com.company.erp.erp.entites.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class HibernateCompanyFilterEnabler extends OncePerRequestFilter {

    @PersistenceContext
    private  EntityManager entityManager;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") ||
                path.equals("/api/company/register-tenant")
                || path.startsWith("/v3/api-docs/")
                || path.startsWith("/swagger-ui/")
                || path.equals("/swagger-ui.html");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Long companyId = TenantContext.getCompanyId();

        if (companyId != null) {
            org.hibernate.Session session = entityManager.unwrap(org.hibernate.Session.class);
            System.out.println("Before Enabling filter for companyId: " + companyId);
            session.enableFilter("companyFilter").setParameter("companyId", companyId);
            System.out.println("Filter enabled? " + session.getEnabledFilter("companyFilter"));
        }

        filterChain.doFilter(request, response);
    }
}
