package com.company.userService.UserService.jwt;

import com.company.erp.erp.entites.Company;
import com.company.erp.erp.entites.Role;
import com.company.erp.erp.entites.TenantContext;
import com.company.erp.erp.entites.User;
import com.company.userService.repository.CompanyRepository;
import com.company.userService.repository.RoleRepository;
import com.company.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    /**
     * Used by Login endpoint (username + companyDomain).
     */
    public UserDetails loadUserByUsernameAndDomain(String username, String domain) throws UsernameNotFoundException {

        Company company = companyRepository.findByDomain(domain)
                .orElseThrow(() -> new UsernameNotFoundException("Company domain not found: " + domain));

        User user = userRepository.findByUsernameAndCompanyId(username, company.getCompanyId())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User " + username + " not found in company " + domain
                ));

        return buildPrincipal(user);
    }

    /**
     * Used by JwtRequestFilter (username + companyId from token).
     */
    public UserDetails loadUserByUsernameAndCompanyId(String username, Long companyId) throws UsernameNotFoundException {

        if (companyId == null) {
            throw new UsernameNotFoundException("CompanyId is required");
        }

        User user = userRepository.findByUsernameAndCompanyId(username, companyId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User " + username + " not found in companyId " + companyId
                ));

        return buildPrincipal(user);
    }

    /**
     * Spring Security interface method.
     * We make it work only when TenantContext already has companyId (set by JwtRequestFilter).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Long companyId = TenantContext.getCompanyId();
        if (companyId == null) {
            throw new UsernameNotFoundException("Missing tenant context. Login requires company domain or companyId.");
        }

        User user = userRepository.findByUsernameAndCompanyId(username, companyId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User " + username + " not found in companyId " + companyId
                ));

        return buildPrincipal(user);
    }

    public boolean userExistsInCompany(String username, Long companyId) {
        return userRepository.findByUsernameAndCompanyId(username, companyId).isPresent();
    }

    public void saveUser(AuthRequest request, String encodedPassword) {
        Company company = companyRepository.findByDomain(request.getCompanyDomain())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // ⚠️ لازم role تكون داخل نفس الشركة
        Role role = roleRepository.findByRoleNameAndCompanyId("SUPER_ADMIN", company.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Default role not found in this company"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setCompanyId(company.getCompanyId());
        user.setIsActive(true);

        userRepository.save(user);
    }

    private UserPrincipal buildPrincipal(User user) {
        return new UserPrincipal(
                user.getUsername(),
                user.getPassword(),
                user.getIsActive(),
                user.getCompanyId(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName())
                )
        );
    }
}