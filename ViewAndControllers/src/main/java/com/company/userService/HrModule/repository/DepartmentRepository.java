package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("""
    SELECT d FROM Department d
    LEFT JOIN FETCH d.manager
    WHERE d.departmentId = :id
    """)
    Optional<Department> findByIdWithManager(@Param("id") Long id);
}
