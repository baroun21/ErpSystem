package com.company.erp.erp.entites;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PAYROLL",
        uniqueConstraints = @UniqueConstraint(name = "UK_PAYROLL_EMP_PERIOD", columnNames = {"EMPLOYEE_ID", "PAY_PERIOD"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(TenantEntityListener.class)
public class Payroll extends BaseEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payroll_seq")
    @SequenceGenerator(name = "payroll_seq", sequenceName = "PAYROLL_SEQ", allocationSize = 1)
    @Column(name = "PAYROLL_ID")
    private Long payrollId;

    // Example: store day of the pay period, or use a string like "2025-09"—your choice.
    @Column(name = "PAY_PERIOD", nullable = false, length = 20)
    private String payPeriod;

    @Column(name = "BASIC_SALARY", precision = 10, scale = 2, nullable = false)
    private BigDecimal basicSalary;

    @Column(name = "OVERTIME_PAY", precision = 10, scale = 2)
    private BigDecimal overtimePay;

    @Column(name = "DEDUCTIONS", precision = 10, scale = 2)
    private BigDecimal deductions;

    @Column(name = "NET_SALARY", precision = 10, scale = 2, nullable = false)
    private BigDecimal netSalary;

    @Column(name = "PAYMENT_DATE", insertable = false)
    private LocalDate paymentDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;
}
