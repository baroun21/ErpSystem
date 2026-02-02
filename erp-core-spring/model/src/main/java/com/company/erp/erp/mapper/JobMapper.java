package com.company.erp.erp.mapper;


import com.company.erp.erp.entites.Job;
import com.company.erp.erp.entites.request.JobRequest;
import com.company.erp.erp.entites.response.JobResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface JobMapper {

    // Request → Entity
    @Mapping(target = "jobId", ignore = true)
    Job toEntity(JobRequest request);

    // Entity → Response
    JobResponse toResponse(Job entity);

    // Update existing entity safely (for PUT/PATCH endpoints)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(JobRequest request, @MappingTarget Job entity);
}

