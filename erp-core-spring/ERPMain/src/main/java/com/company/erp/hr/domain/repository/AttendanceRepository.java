package com.company.erp.hr.domain.repository;

import com.company.erp.hr.domain.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * AttendanceRepository - handles persistence of Attendance records
 * All queries automatically filtered by company_id (multi-tenancy)
 */
@Repository("erpAttendanceRepository")
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Find attendance for specific employee, date, and company
     */
    @Query("SELECT a FROM HrAttendance a WHERE a.companyId = :companyId AND a.employee.id = :employeeId AND a.attendanceDate = :date")
    Optional<Attendance> findByEmployeeAndDateInCompany(
        @Param("companyId") Long companyId,
        @Param("employeeId") Long employeeId,
        @Param("date") LocalDate date
    );

    /**
     * Find attendance records for employee within date range in company
     */
    @Query("SELECT a FROM HrAttendance a WHERE a.companyId = :companyId AND a.employee.id = :employeeId " +
           "AND a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.attendanceDate DESC")
    List<Attendance> findByEmployeeAndDateRangeInCompany(
        @Param("companyId") Long companyId,
        @Param("employeeId") Long employeeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * Find all attendance records for company in date range
     */
    @Query("SELECT a FROM HrAttendance a WHERE a.companyId = :companyId " +
           "AND a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.attendanceDate DESC, a.employee.lastName")
    List<Attendance> findAllInCompanyByDateRange(
        @Param("companyId") Long companyId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * Count present days for employee in date range
     */
    @Query("SELECT COUNT(a) FROM HrAttendance a WHERE a.companyId = :companyId AND a.employee.id = :employeeId " +
           "AND a.status IN ('Present', 'Half Day') AND a.attendanceDate BETWEEN :startDate AND :endDate")
    long countPresentDaysInCompany(
        @Param("companyId") Long companyId,
        @Param("employeeId") Long employeeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
