package com.company.erp.erp.entites.Dtos;

import com.company.erp.erp.enums.CostingMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long productId;
    private String sku;
    private String name;
    private String description;
    private String category;
    private String unitOfMeasure;
    private BigDecimal standardCost;
    private BigDecimal listPrice;
    private CostingMethod costingMethod;
    private BigDecimal reorderPoint;
    private BigDecimal reorderQuantity;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<ProductVariantDTO> variants;
}
