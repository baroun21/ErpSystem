package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // ✅ Explicit tenant-safe findAll (does not rely on @Filter)
    @Query("""
        SELECT e
        FROM Employee e
        WHERE e.companyId = :companyId
    """)
    List<Employee> findAllByCompanyId(@Param("companyId") Long companyId);

    // ✅ Tenant-safe findById
    @Query("""
        SELECT e
        FROM Employee e
        WHERE e.employeeId = :id
          AND e.companyId = :companyId
    """)
    Optional<Employee> findByIdAndCompanyId(@Param("id") Long id,
                                            @Param("companyId") Long companyId);

    // ✅ Tenant-safe findById with attendance + job
    @Query("""
        SELECT e
        FROM Employee e
        LEFT JOIN FETCH e.attendances a
        LEFT JOIN FETCH e.job j
        WHERE e.employeeId = :id
          AND e.companyId = :companyId
    """)
    Optional<Employee> findByIdWithAttendance(@Param("id") Long id,
                                              @Param("companyId") Long companyId);

    // ✅ Tenant-safe delete (native query must include companyId!)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = :id AND COMPANY_ID = :companyId", nativeQuery = true)
    int deleteByIdAndCompanyId(@Param("id") Long id,
                               @Param("companyId") Long companyId);

    // ✅ Tenant-safe manager lookup (if you want a dedicated method)
    @Query("""
        SELECT e
        FROM Employee e
        WHERE e.employeeId = :managerId
          AND e.companyId = :companyId
    """)
    Optional<Employee> findManagerInCompany(@Param("managerId") Long managerId,
                                            @Param("companyId") Long companyId);
}