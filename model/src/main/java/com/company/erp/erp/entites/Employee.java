package com.company.erp.erp.entites;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EMPLOYEE")

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(TenantEntityListener.class)
public class Employee extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "emp_seq", sequenceName = "EMPLOYEES_SEQ", allocationSize = 1), generator = "emp_seq"
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "FIRST_NAME", length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", length = 50, nullable = false)
    private String lastName;

    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "PHONE_NUMBER", length = 30)
    private String phoneNumber;

    @Column(name = "HIRE_DATE", nullable = false)
    private LocalDate hireDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "JOB_ID", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @Column(name = "SALARY", precision = 10, scale = 2)
    private BigDecimal salary;

//     Manager (self reference)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "MANAGER_ID")
    private Employee managerId;

    //  Added: user relationship
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Attendance> attendances = new HashSet<>();

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public Job getJob() {
        return job;
    }

    public Department getDepartment() {
        return department;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Employee getManagerId() {
        return managerId;
    }
    public Employee setManagerId() {
        return managerId;
    }

    public User getUser() {
        return user;
    }
}
