package com.company.userService.UserService.jwt;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String Email;
}

