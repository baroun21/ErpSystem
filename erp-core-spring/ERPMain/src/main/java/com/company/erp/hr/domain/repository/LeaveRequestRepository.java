package com.company.erp.hr.domain.repository;

import com.company.erp.hr.domain.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * LeaveRequestRepository - handles persistence of LeaveRequest entities
 * All queries automatically filtered by company_id (multi-tenancy)
 */
@Repository("erpLeaveRequestRepository")
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    /**
     * Find all leave requests for employee in company
     */
       @Query("SELECT lr FROM HrLeaveRequest lr WHERE lr.companyId = :companyId AND lr.employee.id = :employeeId " +
           "ORDER BY lr.fromDate DESC")
    List<LeaveRequest> findByEmployeeInCompany(@Param("companyId") Long companyId, @Param("employeeId") Long employeeId);

    /**
     * Find pending leave requests for employee in company
     */
       @Query("SELECT lr FROM HrLeaveRequest lr WHERE lr.companyId = :companyId AND lr.employee.id = :employeeId " +
           "AND lr.status = 'Pending' ORDER BY lr.fromDate")
    List<LeaveRequest> findPendingByEmployeeInCompany(@Param("companyId") Long companyId, @Param("employeeId") Long employeeId);

    /**
     * Find all pending leave requests in company (for managers)
     */
       @Query("SELECT lr FROM HrLeaveRequest lr WHERE lr.companyId = :companyId AND lr.status = 'Pending' " +
           "ORDER BY lr.fromDate")
    List<LeaveRequest> findAllPendingInCompany(@Param("companyId") Long companyId);

    /**
     * Find leave requests for date range in company
     */
       @Query("SELECT lr FROM HrLeaveRequest lr WHERE lr.companyId = :companyId " +
           "AND ((lr.fromDate <= :endDate AND lr.toDate >= :startDate)) " +
           "ORDER BY lr.fromDate")
    List<LeaveRequest> findByDateRangeInCompany(
        @Param("companyId") Long companyId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * Count approved leave days for employee in year
     */
    @Query(
        value = "SELECT COALESCE(SUM((lr.to_date - lr.from_date) + 1), 0) " +
                "FROM leave_requests lr " +
                "WHERE lr.company_id = :companyId " +
                "AND lr.employee_id = :employeeId " +
                "AND lr.status = 'Approved' " +
                "AND EXTRACT(YEAR FROM lr.from_date) = :year",
        nativeQuery = true
    )
    Long countApprovedLeaveDaysInYear(
        @Param("companyId") Long companyId,
        @Param("employeeId") Long employeeId,
        @Param("year") Integer year
    );
}

