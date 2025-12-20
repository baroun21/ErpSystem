package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "DEPARTMENT",
        uniqueConstraints = @UniqueConstraint(name = "UK_DEPT_NAME", columnNames = "DEPARTMENT_NAME"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "dept_seq", sequenceName = "DEPARTMENTS_SEQ", allocationSize = 1) , generator = "dept_seq"
    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "DEPARTMENT_NAME", nullable = false, length = 100)
    private String departmentName;

    // Optional: department’s manager (an employee)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER_ID")
    private Employee manager;
}
