package com.company.userService.HrModule.service;

import com.company.erp.erp.entites.Department;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Job;
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

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        // 1. Fetch related entities
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + request.getJobId()));

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findByIdWithManager(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));
        }

        Employee manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + request.getManagerId()));
        }

//        User user = null;
//        if (request.getUserId() != null) {
//            user = userRepository.findById(request.getUserId())
//                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
//        }

        // 2. Map request → entity using MapStruct
        Employee employee = employeeMapper.toEntity(request, job, department,manager);

//        // 3. Set optional manager (since mapper signature doesn’t include it)
//        employee.getEmployeeId();

        // 4. Save entity
        Employee savedEmployee = employeeRepository.save(employee);

        // 5. Map entity → response DTO
        return employeeMapper.toResponse(savedEmployee);
    }


    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeWithAttendance(Long id) {
        Employee employee = employeeRepository.findByIdWithAttendance(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));

        // Map entity → response
        return employeeMapper.toResponse(employee);
    }

    //  READ - all employees
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    //  UPDATE
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + request.getJobId()));

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findByIdWithManager(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));
        }

        Employee manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + request.getManagerId()));
        }

//        User user = null;
//        if (request.getUserId() != null) {
//            user = userRepository.findById(request.getUserId())
//                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
//        }

        // Map changes (ignore nulls)
        employeeMapper.updateEntityFromRequest(request, existingEmployee, job, department, manager);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return employeeMapper.toResponse(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {

        // 1 Check if the employee exists
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));



        // 2 Perform delete (native query)
        employeeRepository.deleteEmployeeById(id);
    }
}
// postman for post request
//{
//  "firstName": "John",
//  "lastName": "Doe",
//  "email": "john.doe@company.com",
//  "phoneNumber": "123456789",
//  "hireDate": "2025-10-12",
//  "jobId": 1,
//  "departmentId": 2,
//  "salary": 5500.00,
//  "managerId": 3,
//  "userId": 4
//}

// for put request
//{
//  "firstName": "Johnathan",
//  "lastName": "Doe",
//  "email": "john.doe@company.com",
//  "phoneNumber": "987654321",
//  "hireDate": "2025-10-12",
//  "jobId": 1,
//  "departmentId": 2,
//  "salary": 6000.00,
//  "managerId": 3,
//  "userId": 4
//}
