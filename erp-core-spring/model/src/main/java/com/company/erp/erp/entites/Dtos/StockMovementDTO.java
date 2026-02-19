package com.company.erp.erp.entites.Dtos;

import com.company.erp.erp.enums.MovementType;
import com.company.erp.erp.enums.MovementStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementDTO {
    private Long movementId;
    private Long productId;
    private String productSku;
    private String productName;
    private Long warehouseId;
    private String warehouseCode;
    private MovementType movementType;
    private String movementNumber;
    private Long fromWarehouseId;
    private String referenceType;
    private String referenceNumber;
    private BigDecimal quantityMoved;
    private BigDecimal quantityReceived;
    private MovementStatus status;
    private String notes;
    private LocalDateTime receivedDate;
    private LocalDateTime expectedDate;
    private String receivedBy;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
