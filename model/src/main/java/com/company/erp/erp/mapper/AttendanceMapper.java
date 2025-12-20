package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.Attendance;
import com.company.erp.erp.entites.request.AttendanceRequest;
import com.company.erp.erp.entites.response.AttendanceResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "attendanceId", ignore = true)
    @Mapping(target = "employee", ignore = true)
    Attendance toEntity(AttendanceRequest request);

    @Mapping(target = "employee", ignore = true)
    AttendanceResponse toResponse(Attendance attendance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(
            AttendanceRequest request,
            @MappingTarget Attendance attendance
    );
}
