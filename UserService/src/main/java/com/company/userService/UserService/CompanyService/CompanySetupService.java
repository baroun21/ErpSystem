package com.company.userService.UserService.CompanyService;

import com.company.erp.erp.entites.Company;
import com.company.erp.erp.entites.Dtos.CompanyRegistrationRequest;
import com.company.erp.erp.entites.Role;
import com.company.erp.erp.entites.User;
import com.company.userService.UserService.jwt.AuthResponse;
import com.company.userService.UserService.jwt.JwtUtil;
import com.company.userService.repository.CompanyRepository;
import com.company.userService.repository.RoleRepository;
import com.company.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanySetupService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse registerNewCompany(CompanyRegistrationRequest request) {

        // 0) Basic validation (خفيف)
        if (request.getCompanyName() == null || request.getCompanyName().isBlank()) {
            throw new RuntimeException("Company name is required");
        }
        if (request.getAdminUsername() == null || request.getAdminUsername().isBlank()) {
            throw new RuntimeException("Admin username is required");
        }
        if (request.getAdminEmail() == null || request.getAdminEmail().isBlank()) {
            throw new RuntimeException("Admin email is required");
        }
        if (request.getAdminPassword() == null || request.getAdminPassword().isBlank()) {
            throw new RuntimeException("Admin password is required");
        }

        // 1) Create Company
        Company company = Company.builder()
                .companyName(request.getCompanyName())
                .domain(request.getDomain())
                .status("ACTIVE")
                .build();

        company = companyRepository.save(company);
        Long companyId = company.getCompanyId();

        // 2) Create Roles for THIS company (tenant-scoped)
        // (اعمل اللي يناسبك من الأدوار، دي مجموعة منطقية)
        Role superAdminRole = roleRepository.save(
                Role.builder()
                        .roleName("SUPER_ADMIN")
                        .description("Company owner with full access")
                        .companyId(companyId)
                        .build()
        );
        System.out.println("Roles before creation for companyId: " + companyId);
        roleRepository.save(Role.builder()
                .roleName("ADMIN")
                .description("Company admin")
                .companyId(companyId)
                .build());
        System.out.println("Roles before creation for companyId: " + companyId);
        roleRepository.save(Role.builder()
                .roleName("HR")
                .description("HR module admin")
                .companyId(companyId)
                .build());
        System.out.println("Roles before creation for companyId: " + companyId);
        roleRepository.save(Role.builder()
                .roleName("INVENTORY")
                .description("Inventory module admin")
                .companyId(companyId)
                .build());

        System.out.println("Roles before creation for companyId: " + companyId);
        roleRepository.save(Role.builder()
                .roleName("FINANCE")
                .description("Finance module admin")
                .companyId(companyId)
                .build());

        roleRepository.save(Role.builder()
                .roleName("USER")
                .description("Default user")
                .companyId(companyId)
                .build());

        // 3) Create First User (Company Owner) as SUPER_ADMIN
        User admin = new User();
        admin.setUsername(request.getAdminUsername());
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        admin.setRole(superAdminRole);
        admin.setCompanyId(companyId);
        admin.setIsActive(true);

        userRepository.save(admin);

        // 4) Generate token that includes companyId claim
        String token = jwtUtil.generateToken(admin.getUsername(), companyId);

        return new AuthResponse(token, "Bearer");
    }
}