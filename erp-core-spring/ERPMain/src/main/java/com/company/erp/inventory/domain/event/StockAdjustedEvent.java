package com.company.erp.inventory.domain.event;

import com.company.erp.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StockAdjustedEvent extends DomainEvent {

    private Long productId;
    private Long warehouseId;
    private String movementType;
    private BigDecimal quantity;
    private String reason;
    private LocalDateTime adjustedAt;

    public StockAdjustedEvent(
        Long stockMovementId,
        Long companyId,
        Long productId,
        Long warehouseId,
        String movementType,
        BigDecimal quantity,
        String reason,
        LocalDateTime adjustedAt,
        String triggeredBy
    ) {
        super(stockMovementId, "StockMovement", companyId, triggeredBy);
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.reason = reason;
        this.adjustedAt = adjustedAt;
    }
}

