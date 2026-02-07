package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Bonus;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.enums.BonusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BonusRepository extends JpaRepository<Bonus, Long> {
    List<Bonus> findByEmployee(Employee employee);

    boolean existsByEmployeeAndBonusTypeAndDateGranted(
            Employee employee,
            BonusType bonusType,
            LocalDate dateGranted
    );
    // Get all bonuses by company (through employee)
    List<Bonus> findAllByEmployeeCompanyId(Long companyId);

    // Get one bonus by id + company
    Optional<Bonus> findByBonusIdAndEmployeeCompanyId(Long bonusId, Long companyId);

    // Get bonuses by employeeId + companyId
    List<Bonus> findByEmployeeEmployeeIdAndEmployeeCompanyId(Long employeeId, Long companyId);

    // Duplicate check tenant-safe (bonusType type depends on your model)
    boolean existsByEmployeeEmployeeIdAndEmployeeCompanyIdAndBonusTypeAndDateGranted(
            Long employee_employeeId, Long employee_companyId, BonusType bonusType, LocalDate dateGranted
    );
}
