package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.InventoryTransaction;
import com.company.erp.erp.entites.Dtos.InventoryTransactionDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InventoryTransactionMapper {
    
    // Entity → DTO
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productSku", source = "product.sku")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "warehouseId", source = "warehouse.warehouseId")
    @Mapping(target = "warehouseCode", source = "warehouse.warehouseCode")
    InventoryTransactionDTO toDTO(InventoryTransaction transaction);
    
    // DTO → Entity
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InventoryTransaction toEntity(InventoryTransactionDTO dto);
    
    // Update
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(InventoryTransactionDTO dto, @MappingTarget InventoryTransaction transaction);
}
