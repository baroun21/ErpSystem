package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.StockMovement;
import com.company.erp.erp.entites.Dtos.StockMovementDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
    
    // Entity → DTO
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productSku", source = "product.sku")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "warehouseId", source = "warehouse.warehouseId")
    @Mapping(target = "warehouseCode", source = "warehouse.warehouseCode")
    StockMovementDTO toDTO(StockMovement movement);
    
    // DTO → Entity
    @Mapping(target = "movementId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StockMovement toEntity(StockMovementDTO dto);
    
    // Update
    @Mapping(target = "movementId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(StockMovementDTO dto, @MappingTarget StockMovement movement);
}
