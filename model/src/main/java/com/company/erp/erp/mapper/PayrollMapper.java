package com.company.erp.erp.mapper;


import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.Payroll;
import com.company.erp.erp.entites.request.PayrollRequest;
import com.company.erp.erp.entites.response.PayrollResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmployeeSummaryMapper.class})
public interface PayrollMapper {

    // Request → Entity
    @Mapping(target = "payrollId", ignore = true)
    @Mapping(target = "employee", source = "employee") // Injected manually from service
    Payroll toEntity(PayrollRequest dto, Employee employee);

    // Entity → Response
    PayrollResponse toResponse(Payroll entity);
}
