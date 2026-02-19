package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.ReorderRule;
import com.company.erp.erp.entites.Dtos.ReorderRuleDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReorderRuleMapper {
    
    // Entity → DTO
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productSku", source = "product.sku")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "warehouseId", source = "warehouse.warehouseId")
    @Mapping(target = "warehouseCode", source = "warehouse.warehouseCode")
    ReorderRuleDTO toDTO(ReorderRule rule);
    
    // DTO → Entity
    @Mapping(target = "ruleId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReorderRule toEntity(ReorderRuleDTO dto);
    
    // Update
    @Mapping(target = "ruleId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(ReorderRuleDTO dto, @MappingTarget ReorderRule rule);
}
