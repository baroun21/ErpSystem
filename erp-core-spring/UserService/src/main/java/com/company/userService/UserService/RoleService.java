package com.company.userService.UserService;


import com.company.erp.erp.entites.Role;

import com.company.erp.erp.entites.request.RoleCreateRequest;
import com.company.erp.erp.entites.response.RoleResponse;
import com.company.userService.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleResponse createRole(RoleCreateRequest dto) {
        Role role = Role.builder().roleName(dto.getRoleName()).description(dto.getDescription()).build();
        Role saved = roleRepository.save(role);
        return new RoleResponse(saved.getRoleId(),
                saved.getRoleName(),
                saved.getDescription());
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(r ->
                new RoleResponse(r.getRoleId(),
                        r.getRoleName(),
                        r.getDescription())).collect(Collectors.toList());
    }

    public RoleResponse getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() ->
                new IllegalArgumentException("Role not found"));
        return
                new RoleResponse(role.getRoleId(),
                        role.getRoleName(),
                        role.getDescription());
    }
}
