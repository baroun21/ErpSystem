package com.company.erp.erp.entites.response;


import com.company.erp.erp.entites.Dtos.EmployeeSummary;
import com.company.erp.erp.enums.BonusType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BonusResponse {

    private Long bonusId;
    private EmployeeSummary employee;
    private BonusType bonusType;
    private BigDecimal amount;
    private LocalDate dateGranted;
}

