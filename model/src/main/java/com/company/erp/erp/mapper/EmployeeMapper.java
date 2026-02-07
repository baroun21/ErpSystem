package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.Attendance;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Job;
import com.company.erp.erp.entites.Department;
import com.company.erp.erp.entites.request.EmployeeRequest;
import com.company.erp.erp.entites.response.EmployeeResponse;
import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {

    @Autowired
    protected AttendanceMapper attendanceMapper;

    // ────────────── Request → Entity ──────────────
    @Mappings({
            @Mapping(target = "employeeId", ignore = true), // auto-generated
            @Mapping(target = "firstName", source = "request.firstName"),
            @Mapping(target = "lastName", source = "request.lastName"),
            @Mapping(target = "email", source = "request.email"),
            @Mapping(target = "phoneNumber", source = "request.phoneNumber"),
            @Mapping(target = "hireDate", source = "request.hireDate"),
            @Mapping(target = "salary", source = "request.salary"),
            @Mapping(target = "job", source = "job"),
            @Mapping(target = "department", source = "department"),
            @Mapping(target = "managerId", source = "manager"),
            @Mapping(target = "user", ignore = true), // map separately if needed
            @Mapping(target = "attendances", ignore = true),// handled separately
            @Mapping(target = "companyId", ignore = true)
    })
  public abstract Employee toEntity(EmployeeRequest request, Job job, Department department, Employee manager);

    // ────────────── Entity → Response ──────────────
    @Mapping(target = "employeeSummary", ignore = true) // handled manually
    public abstract EmployeeResponse toResponse(Employee employee);

    // ────────────── Partial Update ──────────────
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "employeeId", source = "request.employeeId"),
            @Mapping(target = "firstName", source = "request.firstName"),
            @Mapping(target = "lastName", source = "request.lastName"),
            @Mapping(target = "email", source = "request.email"),
            @Mapping(target = "phoneNumber", source = "request.phoneNumber"),
            @Mapping(target = "hireDate", source = "request.hireDate"),
            @Mapping(target = "salary", source = "request.salary"),
            @Mapping(target = "job", source = "job"),
            @Mapping(target = "department", source = "department"),
            @Mapping(target = "managerId", source = "manager"),
            @Mapping(target = "attendances", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "companyId", ignore = true)
    })
    public abstract void updateEntityFromRequest(EmployeeRequest request, @MappingTarget Employee employee, Job job, Department department, Employee manager);

    // ────────────── AfterMapping Helpers ──────────────
    @AfterMapping
    protected void enrichEmployeeResponse(Employee employee, @MappingTarget EmployeeResponse response) {
        if (employee == null) return;

        // EmployeeSummary for self
        EmployeeSummary selfSummary = mapEmployeeToSummary(employee);
        response.setEmployeeSummary(selfSummary);

        // EmployeeSummary for manager
        if (employee.getManagerId() != null) {
            EmployeeSummary managerSummary = mapEmployeeToSummary(employee.getManagerId());
            response.setManagerId(managerSummary);
        }
    }

    // ────────────── Map Employee → EmployeeSummary ──────────────
    protected EmployeeSummary mapEmployeeToSummary(Employee employee) {
        if (employee == null) return null;

        EmployeeSummary summary = new EmployeeSummary();
        summary.setEmployeeId(employee.getEmployeeId());
        summary.setFirstName(employee.getFirstName());
        summary.setLastName(employee.getLastName());
        summary.setEmail(employee.getEmail());
        summary.setJobTitle(employee.getJob() != null ? employee.getJob().getJobTitle() : null);

        if (employee.getAttendances() != null && !employee.getAttendances().isEmpty()) {
            Attendance latest = employee.getAttendances().stream()
                    .filter(a -> a.getAttendanceDate() != null)
                    .max((a1, a2) -> a1.getAttendanceDate().compareTo(a2.getAttendanceDate()))
                    .orElse(null);

            if (latest != null) {
                summary.setAttendance(attendanceMapper.toResponse(latest));
            }
        }

        return summary;
    }
}
