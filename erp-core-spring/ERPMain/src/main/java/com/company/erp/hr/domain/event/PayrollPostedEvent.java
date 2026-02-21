package com.company.erp.hr.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PayrollPostedEvent extends DomainEvent {

    private Long employeeId;
    private BigDecimal netSalary;
    private LocalDate paymentDate;

    public PayrollPostedEvent(
        Long payrollId,
        Long companyId,
        Long employeeId,
        BigDecimal netSalary,
        LocalDate paymentDate,
        String triggeredBy
    ) {
        super(payrollId, "Payroll", companyId, triggeredBy);
        this.employeeId = employeeId;
        this.netSalary = netSalary;
        this.paymentDate = paymentDate;
    }
}

