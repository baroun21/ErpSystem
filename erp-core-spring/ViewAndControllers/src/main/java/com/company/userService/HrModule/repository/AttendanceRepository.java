package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployee_EmployeeId(Long employeeId);
    List<Attendance> findByAttendanceDate(LocalDate date);
}

