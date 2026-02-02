package com.company.erp.erp.entites.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String fullName;
    private Boolean isActive;
    private Long roleId;
}

