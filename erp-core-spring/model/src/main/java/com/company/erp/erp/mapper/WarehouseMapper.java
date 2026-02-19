package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.Warehouse;
import com.company.erp.erp.entites.Dtos.WarehouseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    
    // Entity → DTO
    WarehouseDTO toDTO(Warehouse warehouse);
    
    // DTO → Entity
    @Mapping(target = "warehouseId", ignore = true)
    @Mapping(target = "stockMovements", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Warehouse toEntity(WarehouseDTO dto);
    
    // Update
    @Mapping(target = "warehouseId", ignore = true)
    @Mapping(target = "stockMovements", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(WarehouseDTO dto, @MappingTarget Warehouse warehouse);
}
