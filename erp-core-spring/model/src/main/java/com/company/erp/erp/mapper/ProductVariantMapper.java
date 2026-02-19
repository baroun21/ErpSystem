package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.ProductVariant;
import com.company.erp.erp.entites.Dtos.ProductVariantDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    
    // Entity → DTO
    @Mapping(target = "productId", source = "product.productId")
    ProductVariantDTO toDTO(ProductVariant variant);
    
    // DTO → Entity
    @Mapping(target = "variantId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductVariant toEntity(ProductVariantDTO dto);
    
    // Update
    @Mapping(target = "variantId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(ProductVariantDTO dto, @MappingTarget ProductVariant variant);
}
