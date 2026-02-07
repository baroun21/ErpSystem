package com.company.userService.HrModule.service;

import com.company.erp.erp.entites.Bonus;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.TenantContext;
import com.company.erp.erp.entites.request.BonusRequest;
import com.company.erp.erp.entites.response.BonusResponse;
import com.company.erp.erp.mapper.BonusMapper;
import com.company.userService.HrModule.exceptions.DuplicateRecordException;
import com.company.userService.HrModule.exceptions.ResourceNotFoundException;
import com.company.userService.HrModule.repository.BonusRepository;
import com.company.userService.HrModule.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BonusService {

    private final BonusRepository bonusRepository;
    private final EmployeeRepository employeeRepository;
    private final BonusMapper bonusMapper;

    private Long currentCompanyIdOrThrow() {
        Long companyId = TenantContext.getCompanyId();
        if (companyId == null) {
            throw new IllegalStateException("TenantContext.companyId is null (JWT missing or filter order issue)");
        }
        return companyId;
    }

    /**
     * Create a new bonus for an employee (tenant-safe).
     */
    public BonusResponse createBonus(BonusRequest request) {
        Long companyId = currentCompanyIdOrThrow();

        // ✅ Employee must belong to the same company
        Employee employee = employeeRepository.findByIdAndCompanyId(request.getEmployeeId(), companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + request.getEmployeeId() + " in this company"));

        LocalDate today = LocalDate.now();

        // ✅ Duplicate check must also be tenant-safe
        boolean exists = bonusRepository.existsByEmployeeEmployeeIdAndEmployeeCompanyIdAndBonusTypeAndDateGranted(
                employee.getEmployeeId(), companyId, request.getBonusType(), today
        );

        if (exists) {
            throw new DuplicateRecordException(
                    "This employee already has a bonus of type " + request.getBonusType() + " today");
        }

        Bonus bonus = bonusMapper.toEntity(request);
        bonus.setEmployee(employee);
        bonus.setDateGranted(today);

        Bonus saved = bonusRepository.save(bonus);
        return bonusMapper.toResponse(saved);
    }

    /**
     * Get all bonuses in current company (tenant-safe).
     */
    @Transactional(readOnly = true)
    public List<BonusResponse> getAllBonuses() {
        Long companyId = currentCompanyIdOrThrow();

        return bonusRepository.findAllByEmployeeCompanyId(companyId).stream()
                .map(bonusMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get bonus by ID (tenant-safe).
     */
    @Transactional(readOnly = true)
    public BonusResponse getBonusById(Long bonusId) {
        Long companyId = currentCompanyIdOrThrow();

        Bonus bonus = bonusRepository.findByBonusIdAndEmployeeCompanyId(bonusId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Bonus not found with ID: " + bonusId + " in this company"));

        return bonusMapper.toResponse(bonus);
    }

    /**
     * Get bonuses by employee ID (tenant-safe).
     */
    @Transactional(readOnly = true)
    public List<BonusResponse> getBonusesByEmployeeId(Long employeeId) {
        Long companyId = currentCompanyIdOrThrow();

        // ✅ Ensure employee belongs to this tenant
        Employee employee = employeeRepository.findByIdAndCompanyId(employeeId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found in this company with ID: " + employeeId));

        return bonusRepository.findByEmployeeEmployeeIdAndEmployeeCompanyId(employee.getEmployeeId(), companyId).stream()
                .map(bonusMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update bonus details (tenant-safe).
     */
    public BonusResponse updateBonus(Long bonusId, BonusRequest request) {
        Long companyId = currentCompanyIdOrThrow();

        Bonus bonus = bonusRepository.findByBonusIdAndEmployeeCompanyId(bonusId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Bonus not found with ID: " + bonusId + " in this company"));

        bonusMapper.updateEntityFromRequest(request, bonus);

        Bonus saved = bonusRepository.save(bonus);
        return bonusMapper.toResponse(saved);
    }

    /**
     * Delete bonus (tenant-safe hard delete).
     * If you want soft delete, add an "active" flag and update it instead.
     */
    public void deleteBonus(Long bonusId) {
        Long companyId = currentCompanyIdOrThrow();

        Bonus bonus = bonusRepository.findByBonusIdAndEmployeeCompanyId(bonusId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Bonus not found with ID: " + bonusId + " in this company"));

        bonusRepository.delete(bonus);
    }
}