package com.company.userService.HrModule.service;


import com.company.erp.erp.entites.Department;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import com.company.erp.erp.entites.request.DepartmentRequest;
import com.company.erp.erp.entites.response.DepartmentResponse;
import com.company.erp.erp.mapper.DepartmentMapper;
import com.company.userService.HrModule.exceptions.ResourceNotFoundException;
import com.company.userService.HrModule.repository.DepartmentRepository;
import com.company.userService.HrModule.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    /**
     * Create a new department
     */
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        Employee manager = null;

        // Handle optional manager
        if (request.getManagerId() != null) {
            manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager not found with ID: " + request.getManagerId()));
        }

        Department department = departmentMapper.toEntity(request, manager);
        Department saved = departmentRepository.save(department);
        return mapToResponse(saved);
    }

    /**
     * Get all departments
     */
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get department by ID
     */
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return mapToResponse(department);
    }

    /**
     * Update existing department
     */
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        Employee manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager not found with ID: " + request.getManagerId()));
        }

        departmentMapper.updateEntityFromRequest(request, department, manager);

        Department updated = departmentRepository.save(department);
        return mapToResponse(updated);
    }

    /**
     * Delete a department
     */
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        departmentRepository.delete(department);
    }

    /**
     * Helper: map Department entity to DepartmentResponse
     */
    private DepartmentResponse mapToResponse(Department department) {
        DepartmentResponse response = departmentMapper.toResponse(department);

        if (department.getManager() != null) {
            Employee manager = department.getManager();
            EmployeeSummary summary = EmployeeSummary.builder()
                    .employeeId(manager.getEmployeeId())
                    .firstName(manager.getFirstName())
                    .lastName(manager.getLastName())
                    .jobTitle(manager.getJob() != null ? manager.getJob().getJobTitle() : null)
                    .build();
            response.setManager(summary);
        }

        return response;
    }
}

