package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JOB")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "job_seq", sequenceName = "JOBS_SEQ", allocationSize = 1) , generator = "job_seq"
    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "JOB_TITLE", nullable = false, length = 100)
    private String jobTitle;

    @Column(name = "MIN_SALARY", precision = 10, scale = 2)
    private BigDecimal minSalary;

    @Column(name = "MAX_SALARY", precision = 10, scale = 2)
    private BigDecimal maxSalary;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }
}
