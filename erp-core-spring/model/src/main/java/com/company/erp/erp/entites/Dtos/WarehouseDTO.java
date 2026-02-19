package com.company.erp.erp.entites.Dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseDTO {
    private Long warehouseId;
    private String warehouseCode;
    private String name;
    private String location;
    private String address;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String managerName;
    private String managerEmail;
    private String managerPhone;
    private Boolean isActive;
    private Boolean isReceiving;
    private Boolean isShipping;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
