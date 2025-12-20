package com.company.erp.erp.entites.request;


import com.company.erp.erp.enums.BonusType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BonusRequest {

//    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Bonus type is required")
    private BonusType bonusType;

    @NotNull(message = "Bonus amount is required")
    @Positive(message = "Bonus amount must be positive")
    @DecimalMin(value = "0.0", inclusive = false, message = "Bonus amount must be greater than 0")
    private BigDecimal amount;
}

