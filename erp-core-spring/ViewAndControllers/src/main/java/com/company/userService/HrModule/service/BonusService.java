package com.company.userService.HrModule.service;

import com.company.erp.erp.entites.Bonus;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.request.BonusRequest;
import com.company.erp.erp.entites.response.BonusResponse;
import com.company.erp.erp.mapper.BonusMapper;
import com.company.userService.HrModule.exceptions.ResourceNotFoundException;
import com.company.userService.HrModule.exceptions.DuplicateRecordException;
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

    /**
     * Create a new bonus for an employee.
     */
    public BonusResponse createBonus(BonusRequest request) {
        Employee employee = employeeRepository.findByIdWithJob(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + request.getEmployeeId()));

        // Optional: check if the employee already received a bonus of the same type and date
        boolean exists = bonusRepository.existsByEmployeeAndBonusTypeAndDateGranted(
                employee, request.getBonusType(), LocalDate.now()
        );
        if (exists) {
            throw new DuplicateRecordException("This employee already has a bonus of type " + request.getBonusType() + " today");
        }

        Bonus bonus = bonusMapper.toEntity(request);
        bonus.setEmployee(employee);
        bonus.setDateGranted(LocalDate.now());

        bonusRepository.save(bonus);
        return bonusMapper.toResponse(bonus);
    }

    /**
     * Get all bonuses.
     */
    @Transactional(readOnly = true)
    public List<BonusResponse> getAllBonuses() {
        return bonusRepository.findAll().stream()
                .map(bonusMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get bonus by ID.
     */
    @Transactional(readOnly = true)
    public BonusResponse getBonusById(Long id) {
        Bonus bonus = bonusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bonus not found with ID: " + id));
        return bonusMapper.toResponse(bonus);
    }

    /**
     * Get bonuses by employee ID.
     */
    @Transactional(readOnly = true)
    public List<BonusResponse> getBonusesByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        return bonusRepository.findByEmployee(employee).stream()
                .map(bonusMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update bonus details.
     */
    public BonusResponse updateBonus(Long id, BonusRequest request) {
        Bonus bonus = bonusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bonus not found with ID: " + id));

        bonusMapper.updateEntityFromRequest(request, bonus);
        bonusRepository.save(bonus);
        return bonusMapper.toResponse(bonus);
    }

    /**
     * Soft the delete (mark as inactive).
     * If you don’t have status, we can replace this with a hard delete.
     */
    public void deleteBonus(Long id) {
        Bonus bonus = bonusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bonus not found with ID: " + id));
        bonusRepository.delete(bonus);
    }
}
