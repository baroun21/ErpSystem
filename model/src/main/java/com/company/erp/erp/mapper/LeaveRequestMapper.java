package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.LeaveRequest;

import com.company.erp.erp.entites.request.LeaveRequestRequest;
import com.company.erp.erp.entites.response.LeaveRequestResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {EmployeeSummaryMapper.class})
public interface LeaveRequestMapper {

    @Mapping(target = "leaveId", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "employee", source = "employee")
    LeaveRequest toEntity(LeaveRequestRequest dto, Employee employee);

    LeaveRequestResponse toResponse(LeaveRequest entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(LeaveRequestRequest dto, @MappingTarget LeaveRequest entity);


}
