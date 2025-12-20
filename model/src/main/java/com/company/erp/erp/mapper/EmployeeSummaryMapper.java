package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmployeeSummaryMapper {

    @Named("employeeToSummary")
    @Mapping(target = "employeeId", source = "employeeId")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "jobTitle", source = "job", qualifiedByName = "extractJobTitle")
    @Mapping(target = "attendance", ignore = true)
        // filled manually
    EmployeeSummary toSummary(Employee employee);

    @Named("extractJobTitle")
    default String extractJobTitle(Job job) {
        return job != null ? job.getJobTitle() : null;
    }
}
