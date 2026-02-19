package com.company.erp.hr.domain.repository;

import com.company.erp.hr.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DepartmentRepository - handles persistence of Department entities
 * All queries automatically filtered by company_id (multi-tenancy)
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Find department by code within company
     */
    @Query("SELECT d FROM Department d WHERE d.companyId = :companyId AND d.code = :code")
    Optional<Department> findByCodeInCompany(@Param("companyId") Long companyId, @Param("code") String code);

    /**
     * Find all departments in company ordered by name
     */
    @Query("SELECT d FROM Department d WHERE d.companyId = :companyId ORDER BY d.name")
    List<Department> findAllInCompany(@Param("companyId") Long companyId);

    /**
     * Find department by id and company_id (ensures multi-tenant safety)
     */
    @Query("SELECT d FROM Department d WHERE d.id = :id AND d.companyId = :companyId")
    Optional<Department> findByIdInCompany(@Param("id") Long id, @Param("companyId") Long companyId);

    /**
     * Count departments in company
     */
    @Query("SELECT COUNT(d) FROM Department d WHERE d.companyId = :companyId")
    long countInCompany(@Param("companyId") Long companyId);
}
