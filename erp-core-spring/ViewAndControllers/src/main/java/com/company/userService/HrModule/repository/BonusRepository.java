package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Bonus;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.enums.BonusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BonusRepository extends JpaRepository<Bonus, Long> {
    List<Bonus> findByEmployee(Employee employee);

    boolean existsByEmployeeAndBonusTypeAndDateGranted(
            Employee employee,
            BonusType bonusType,
            LocalDate dateGranted
    );
}
