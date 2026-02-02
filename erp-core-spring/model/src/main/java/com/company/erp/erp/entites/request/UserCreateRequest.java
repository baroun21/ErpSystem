package com.company.erp.erp.entites.request;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Long roleId;
}
