package com.company.userService.UserService.jwt;

import com.company.erp.erp.entites.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/")
                || path.equals("/api/company/register-tenant")
                || path.startsWith("/v3/api-docs/")
                || path.startsWith("/swagger-ui/")
                || path.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(7);

            String username;
            Long companyId;

            try {
                username = jwtUtil.extractUsername(jwt);
                companyId = jwtUtil.extractCompanyId(jwt);
            } catch (Exception e) {
                filterChain.doFilter(request, response);
                return;
            }

            if (username == null || companyId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            // load user inside the company from DB
            UserDetails userDetails = userDetailsService.loadUserByUsernameAndCompanyId(username, companyId);

            // validate token (minimum: validate subject + expiration)
            if (!jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                filterChain.doFilter(request, response);
                return;
            }

            // set tenant ONLY after token is validated
            TenantContext.setCompanyId(companyId);
            System.out.println("Set TenantContext with companyId: " + companyId);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }
}