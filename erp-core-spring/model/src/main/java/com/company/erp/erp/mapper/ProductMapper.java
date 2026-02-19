package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.Product;
import com.company.erp.erp.entites.Dtos.ProductDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ProductVariantMapper.class})
public interface ProductMapper {
    
    // Entity → DTO
    ProductDTO toDTO(Product product);
    
    // DTO → Entity
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "reorderRules", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductDTO dto);
    
    // Update
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "reorderRules", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget Product product);
}
