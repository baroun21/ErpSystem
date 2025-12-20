package com.company.userService.HrModule.repository;

import com.company.erp.erp.entites.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
