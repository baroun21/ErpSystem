package com.company.erp.erp.entites.Dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDTO {
    private Long variantId;
    private Long productId;
    private String variantSku;
    private String name;
    private String description;
    private String size;
    private String color;
    private BigDecimal weight;
    private String dimensions;
    private BigDecimal cost;
    private BigDecimal price;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
