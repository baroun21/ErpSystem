package com.company.erp.hr.domain.repository;

import com.company.erp.hr.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * EmployeeRepository - handles persistence of Employee entities
 * All queries automatically filtered by company_id (multi-tenancy)
 */
@Repository("erpEmployeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find employee by email within company (multi-tenant filter)
     */
    @Query("SELECT e FROM HrEmployee e WHERE e.companyId = :companyId AND e.email = :email")
    Optional<Employee> findByEmailInCompany(@Param("companyId") Long companyId, @Param("email") String email);

    /**
     * Find all employees in a department within company
     */
    @Query("SELECT e FROM HrEmployee e WHERE e.companyId = :companyId AND e.department.id = :departmentId")
    List<Employee> findByDepartmentInCompany(@Param("companyId") Long companyId, @Param("departmentId") Long departmentId);

    /**
     * Find all employees in a position within company
     */
    @Query("SELECT e FROM HrEmployee e WHERE e.companyId = :companyId AND e.position.id = :positionId")
    List<Employee> findByPositionInCompany(@Param("companyId") Long companyId, @Param("positionId") Long positionId);

    /**
     * Find all active employees in company
     */
    @Query("SELECT e FROM HrEmployee e WHERE e.companyId = :companyId ORDER BY e.lastName, e.firstName")
    List<Employee> findAllInCompany(@Param("companyId") Long companyId);

    /**
     * Count total employees in company
     */
    @Query("SELECT COUNT(e) FROM HrEmployee e WHERE e.companyId = :companyId")
    long countInCompany(@Param("companyId") Long companyId);
}

