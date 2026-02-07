package com.company.userService.UserService.controller;

import com.company.erp.erp.entites.Company;
import com.company.erp.erp.entites.Dtos.CompanyRegistrationRequest;
import com.company.erp.erp.entites.TenantContext;
import com.company.userService.UserService.CompanyService.CompanySetupService;
import com.company.userService.UserService.jwt.AuthResponse;
import com.company.userService.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final CompanySetupService setupService;

    @PostMapping("/register-tenant") // This must be permitted in SecurityConfig
    public ResponseEntity<AuthResponse> registerTenant(@RequestBody CompanyRegistrationRequest request) {
        return ResponseEntity.ok(setupService.registerNewCompany(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @GetMapping("/me")
    public Company getMyCompany() {
        Long companyId = TenantContext.getCompanyId();

        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @PutMapping("/me")
    public Company updateMyCompany(@RequestBody Company updated) {
        Long companyId = TenantContext.getCompanyId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setCompanyName(updated.getCompanyName());
        company.setDomain(updated.getDomain());
        company.setStatus(updated.getStatus());

        return companyRepository.save(company);
    }
}

