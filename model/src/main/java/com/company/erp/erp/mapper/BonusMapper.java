package com.company.erp.erp.mapper;



import com.company.erp.erp.entites.Bonus;
import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import com.company.erp.erp.entites.Employee;
import com.company.erp.erp.entites.request.BonusRequest;
import com.company.erp.erp.entites.response.BonusResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BonusMapper {

    @Mapping(target = "bonusId", ignore = true)
    @Mapping(target = "employee", ignore = true) // will be set in the service
    @Mapping(target = "dateGranted", ignore = true) // handled by DB
    Bonus toEntity(BonusRequest request);


    @Mapping(target = "employee", source = "employee", qualifiedByName = "mapEmployeeToSummary")
    BonusResponse toResponse(Bonus bonus);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(BonusRequest request, @MappingTarget Bonus bonus);

    @Named("mapEmployeeToSummary")
    default EmployeeSummary mapEmployeeToSummary(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeSummary summary = new EmployeeSummary();
        summary.setEmployeeId(employee.getEmployeeId());
        summary.setFirstName(employee.getFirstName());
        summary.setLastName(employee.getLastName());
        summary.setEmail(employee.getEmail());

        if (employee.getJob() != null) {
            summary.setJobTitle(employee.getJob().getJobTitle());
        } else {
            summary.setJobTitle("Unknown");
        }

        return summary;
    }
}

