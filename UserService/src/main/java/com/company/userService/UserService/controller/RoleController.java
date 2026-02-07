package com.company.userService.UserService.controller;

import com.company.erp.erp.entites.request.RoleCreateRequest;
import com.company.erp.erp.entites.response.RoleResponse;
import com.company.userService.UserService.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Endpoints to manage user roles")
public class RoleController {

    private final RoleService roleService;

    // Only ADMIN can create roles
    @Operation(summary = "Create a new role", description = "Only users with ADMIN role can create roles")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleCreateRequest dto) {
        return ResponseEntity.ok(roleService.createRole(dto));
    }

    // ADMIN and HR can view roles
    @Operation(summary = "Get all roles", description = "Users with ADMIN or HR roles can view all roles")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // ADMIN and HR can view a role
    @Operation(summary = "Get role by ID", description = "Users with ADMIN or HR roles can view a role by its ID")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HR')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }
}