package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    //  Check if payroll already exists for the same employee and pay period
    boolean existsByEmployee_EmployeeIdAndPayPeriod(Long employeeId, String payPeriod);
}
