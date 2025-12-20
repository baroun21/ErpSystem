package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = :id", nativeQuery = true)
    void deleteEmployeeById(@Param("id") Long id);

//    @Query(
//            value = """
//            SELECT
//                e.EMPLOYEE_ID,
//                e.FIRST_NAME,
//                e.LAST_NAME,
//                e.EMAIL,
//                e.PHONE_NUMBER,
//                e.HIRE_DATE,
//                e.SALARY,
//                e.JOB_ID,
//                e.DEPARTMENT_ID,
//                a.ATTENDANCE_ID,
//                a.ATTENDANCE_DATE,
//                a.CHECK_IN_TIME,
//                a.CHECK_OUT_TIME,
//                a.HOURS_WORKED
//            FROM EMPLOYEE e
//            LEFT JOIN ATTENDANCE a
//                ON e.EMPLOYEE_ID = a.EMPLOYEE_ID
//            WHERE e.EMPLOYEE_ID = :id
//            """,
//            nativeQuery = true
//    )
//    Optional<Employee> findByIdWithAttendance(@Param("id") Long id);

    @Query("""
    SELECT e
    FROM Employee e
    LEFT JOIN FETCH e.attendances a
    LEFT JOIN FETCH e.job j
    WHERE e.employeeId = :id
    """)
    Optional<Employee> findByIdWithAttendance(@Param("id") Long id);

    @Query(
            value = "SELECT e.* FROM EMPLOYEE e " +
                    "JOIN JOB j ON e.JOB_ID = j.JOB_ID " +
                    "WHERE e.EMPLOYEE_ID = :id",
            nativeQuery = true
    )
    Optional<Employee> findByIdWithJob(@Param("id") Long id);

}
