package com.company.userService.UserService.controller;


import com.company.erp.erp.entites.request.UserCreateRequest;
import com.company.erp.erp.entites.request.UserUpdateRequest;
import com.company.erp.erp.entites.response.UserResponse;
import com.company.userService.UserService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints to manage users")
public class UserController {

    private final UserService userService;

    // Only ADMIN can create users
    @Operation(summary = "Create a new user", description = "Only users with SUPER_ADMIN role can create users")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    // Only ADMIN can update users
    @Operation(summary = "Update user", description = "Only users with SUPER_ADMIN role can update users")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @RequestBody UserUpdateRequest dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // Only ADMIN can delete users
    @Operation(summary = "Delete user", description = "Only users with SUPER_ADMIN role can delete users")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN and HR can view all users
    @Operation(summary = "Get all users", description = "Users with ADMIN or HR roles can view all users")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ADMIN and HR can view a single user
    @Operation(summary = "Get user by ID", description = "Users with ADMIN or HR roles can view a single user by ID")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
