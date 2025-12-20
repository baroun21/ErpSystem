package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.LeaveRequest;
import com.company.erp.erp.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(Employee employee);

    @Query("""
        SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END
        FROM LeaveRequest l
        WHERE l.employee.employeeId = :employeeId
          AND l.startDate <= :endDate
          AND l.endDate >= :startDate
    """)
    boolean existsByEmployeeAndDateRange(@Param("employeeId") Long employeeId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    List<LeaveRequest> findByEmployee_EmployeeId(Long employeeId);
    List<LeaveRequest> findByStatus(LeaveStatus status);
    List<LeaveRequest> findByEmployee_EmployeeIdAndStatus(Long employeeId, LeaveStatus status);

}

