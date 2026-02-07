package com.company.userService.repository;

import com.company.erp.erp.entites.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    // الميثود اللي بتدور بالدومين (زي: vodafone.com أو tech-cloud)
    Optional<Company> findByDomain(String domain);

    // لو حبيت تتأكد إن الدومين موجود قبل ما تكريت شركة جديدة
    boolean existsByDomain(String domain);
}
