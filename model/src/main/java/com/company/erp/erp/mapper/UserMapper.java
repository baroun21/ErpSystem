package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.Role;
import com.company.erp.erp.entites.User;
import com.company.erp.erp.entites.request.UserCreateRequest;
import com.company.erp.erp.entites.response.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .isActive(user.getIsActive())
                .roleName(user.getRole().getRoleName())
                .build();
    }

    public User fromCreateRequest(UserCreateRequest dto, Role role) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword()) // hash later
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .isActive(true)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
