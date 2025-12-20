package com.company.erp.erp.mapper;


import com.company.erp.erp.entites.Department;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import com.company.erp.erp.entites.request.DepartmentRequest;
import com.company.erp.erp.entites.response.DepartmentResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    // Request → Entity
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "manager", source = "manager")
    Department toEntity(DepartmentRequest request, Employee manager);

    // Entity → Response
    @Mapping(target = "manager", expression = "java(mapManagerToSummary(department.getManager()))")
    DepartmentResponse toResponse(Department department);

    // Update existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(DepartmentRequest request, @MappingTarget Department department, Employee manager);

    // Helper for manager summary
    default EmployeeSummary mapManagerToSummary(Employee manager) {
        if (manager == null) return null;
        EmployeeSummary summary = new EmployeeSummary();
        summary.setEmployeeId(manager.getEmployeeId());
        summary.setFirstName(manager.getFirstName());
        summary.setLastName(manager.getLastName());
        summary.setEmail(manager.getEmail());
        return summary;
    }
}

