package com.company.userService.HrModule.service;

import com.company.erp.erp.entites.Department;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Job;
import com.company.erp.erp.entites.TenantContext;
import com.company.erp.erp.entites.request.EmployeeRequest;
import com.company.erp.erp.entites.response.EmployeeResponse;
import com.company.erp.erp.mapper.EmployeeMapper;
import com.company.userService.HrModule.exceptions.ResourceNotFoundException;
import com.company.userService.HrModule.repository.DepartmentRepository;
import com.company.userService.HrModule.repository.EmployeeRepository;
import com.company.userService.HrModule.repository.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    private Long currentCompanyIdOrThrow() {
        Long companyId = TenantContext.getCompanyId();
        if (companyId == null) {
            throw new IllegalStateException("TenantContext.companyId is null (JWT/filter missing?)");
        }
        return companyId;
    }

    // ✅ CREATE (tenant-safe references)
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Long companyId = currentCompanyIdOrThrow();

        // If Job / Department are tenant-scoped too, you MUST fetch them tenant-safe.
        // If they are global lookup tables, normal findById is fine.
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + request.getJobId()));

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findByIdWithManager(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));
        }

        Employee manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findManagerInCompany(request.getManagerId(), companyId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager not found in this company with ID: " + request.getManagerId()));
        }

        Employee employee = employeeMapper.toEntity(request, job, department, manager);

        // Optional: if your listener sets companyId, you can rely on it.
        // But setting explicitly is extra safety:
        employee.setCompanyId(companyId);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponse(savedEmployee);
    }

    // ✅ READ ONE (tenant-safe)
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeWithAttendance(Long id) {
        Long companyId = currentCompanyIdOrThrow();

        Employee employee = employeeRepository.findByIdWithAttendance(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found with ID: " + id + " in companyId: " + companyId));

        return employeeMapper.toResponse(employee);
    }

    // ✅ READ ALL (tenant-safe, does NOT rely on @Filter)
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        Long companyId = currentCompanyIdOrThrow();

        return employeeRepository.findAllByCompanyId(companyId)
                .stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ UPDATE (tenant-safe)
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Long companyId = currentCompanyIdOrThrow();

        Employee existingEmployee = employeeRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + id + " in this company"));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + request.getJobId()));

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findByIdWithManager(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));
        }

        Employee manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findManagerInCompany(request.getManagerId(), companyId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager not found in this company with ID: " + request.getManagerId()));
        }

        employeeMapper.updateEntityFromRequest(request, existingEmployee, job, department, manager);

        // Ensure tenant never changes
        existingEmployee.setCompanyId(companyId);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toResponse(updatedEmployee);
    }

    // ✅ DELETE (tenant-safe)
    public void deleteEmployee(Long id) {
        Long companyId = currentCompanyIdOrThrow();

        int deleted = employeeRepository.deleteByIdAndCompanyId(id, companyId);
        if (deleted == 0) {
            throw new ResourceNotFoundException("Employee not found in this company with ID: " + id);
        }
    }
}