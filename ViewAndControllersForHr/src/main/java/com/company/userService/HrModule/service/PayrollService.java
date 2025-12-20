package com.company.userService.HrModule.service;

import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Payroll;
import com.company.erp.erp.entites.request.PayrollRequest;
import com.company.erp.erp.entites.response.PayrollResponse;
import com.company.erp.erp.mapper.PayrollMapper;

import com.company.userService.HrModule.exceptions.DuplicateRecordException;
import com.company.userService.HrModule.exceptions.ResourceNotFoundException;
import com.company.userService.HrModule.repository.EmployeeRepository;
import com.company.userService.HrModule.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollMapper payrollMapper;

    // 🔹 CREATE Payroll
    public PayrollResponse createPayroll(PayrollRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + request.getEmployeeId()));

        // Check duplicate payroll
        boolean exists = payrollRepository.existsByEmployee_EmployeeIdAndPayPeriod(
                request.getEmployeeId(), request.getPayPeriod());

        if (exists) {
            throw new DuplicateRecordException(
                    "Payroll already exists for employee ID " + request.getEmployeeId() +
                            " and pay period " + request.getPayPeriod());
        }

        // Auto-calculate net salary
        BigDecimal overtime = request.getOvertimePay() != null ? request.getOvertimePay() : BigDecimal.ZERO;
        BigDecimal deductions = request.getDeductions() != null ? request.getDeductions() : BigDecimal.ZERO;
        BigDecimal netSalary = request.getBasicSalary()
                .add(overtime)
                .subtract(deductions);

        Payroll payroll = payrollMapper.toEntity(request, employee);
        payroll.setNetSalary(netSalary);
        payroll.setPaymentDate(LocalDate.now());

        Payroll saved = payrollRepository.save(payroll);
        return payrollMapper.toResponse(saved);
    }

    // 🔹 GET all payrolls
    @Transactional(readOnly = true)
    public List<PayrollResponse> getAllPayrolls() {
        return payrollRepository.findAll()
                .stream()
                .map(payrollMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 🔹 GET payroll by ID
    @Transactional(readOnly = true)
    public PayrollResponse getPayrollById(Long payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payroll not found with ID: " + payrollId));
        return payrollMapper.toResponse(payroll);
    }

    // 🔹 UPDATE Payroll
    public PayrollResponse updatePayroll(Long payrollId, PayrollRequest request) {
        Payroll existingPayroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payroll not found with ID: " + payrollId));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + request.getEmployeeId()));

        // Update fields
        existingPayroll.setEmployee(employee);
        existingPayroll.setPayPeriod(request.getPayPeriod());
        existingPayroll.setBasicSalary(request.getBasicSalary());
        existingPayroll.setOvertimePay(request.getOvertimePay());
        existingPayroll.setDeductions(request.getDeductions());

        // Recalculate net salary
        BigDecimal overtime = request.getOvertimePay() != null ? request.getOvertimePay() : BigDecimal.ZERO;
        BigDecimal deductions = request.getDeductions() != null ? request.getDeductions() : BigDecimal.ZERO;
        BigDecimal netSalary = request.getBasicSalary()
                .add(overtime)
                .subtract(deductions);

        existingPayroll.setNetSalary(netSalary);
        existingPayroll.setPaymentDate(LocalDate.now()); // Optional — refresh payment date on update

        Payroll updated = payrollRepository.save(existingPayroll);
        return payrollMapper.toResponse(updated);
    }

    // 🔹 DELETE Payroll
    public void deletePayroll(Long payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payroll not found with ID: " + payrollId));
        payrollRepository.delete(payroll);
    }
}

