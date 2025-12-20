package com.company.userService.HrModule.service;



import com.company.erp.erp.entites.Job;
import com.company.erp.erp.entites.request.JobRequest;
import com.company.erp.erp.entites.response.JobResponse;

import com.company.erp.erp.mapper.JobMapper;
import com.company.userService.HrModule.exceptions.BadRequestException;
import com.company.userService.HrModule.exceptions.ResourceNotFoundException;
import com.company.userService.HrModule.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    //  CREATE
    public JobResponse createJob(JobRequest request) {
        validateSalaryRange(request.getMinSalary(), request.getMaxSalary());

        Job job = jobMapper.toEntity(request);
        Job savedJob = jobRepository.save(job);

        return jobMapper.toResponse(savedJob);
    }

    //  READ ALL
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(jobMapper::toResponse)
                .collect(Collectors.toList());
    }

    //  READ ONE
    @Transactional(readOnly = true)
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));
        return jobMapper.toResponse(job);
    }

    //  UPDATE
    public JobResponse updateJob(Long id, JobRequest request) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));

        validateSalaryRange(request.getMinSalary(), request.getMaxSalary());

        jobMapper.updateEntityFromRequest(request, existingJob);

        Job updatedJob = jobRepository.save(existingJob);

        return jobMapper.toResponse(updatedJob);
    }

    //  DELETE
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));

        // Add optional rule if jobs are get by employees

        if (job.getJobTitle().equalsIgnoreCase("CEO")) {
            throw new BadRequestException("Cannot delete critical job position: " + job.getJobTitle());
        }

        jobRepository.delete(job);
    }

    // 🔸 Helper method for validation
    private void validateSalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        if (minSalary != null && maxSalary != null && minSalary.compareTo(maxSalary) > 0) {
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }
    }
}

