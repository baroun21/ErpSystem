package com.company.erp.erp.entites.Dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReorderRuleDTO {
    private Long ruleId;
    private Long productId;
    private String productSku;
    private String productName;
    private Long warehouseId;
    private String warehouseCode;
    private BigDecimal reorderLevel;
    private BigDecimal reorderQuantity;
    private Integer leadTimeDays;
    private BigDecimal safetyStock;
    private BigDecimal maxStock;
    private BigDecimal economicOrderQuantity;
    private Boolean isAutomatic;
    private Boolean isActive;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
