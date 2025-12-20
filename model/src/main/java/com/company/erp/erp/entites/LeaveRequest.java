package com.company.erp.erp.entites;

import com.company.erp.erp.enums.LeaveStatus;
import com.company.erp.erp.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "LEAVE_REQUESTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "leave_seq", sequenceName = "LEAVE_REQUESTS_SEQ", allocationSize = 1), generator = "leave_seq"
    @Column(name = "LEAVE_ID")
    private Long leaveId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "LEAVE_TYPE", length = 20, nullable = false)
    private LeaveType leaveType;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

}
