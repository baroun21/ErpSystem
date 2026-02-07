package com.company.userService.UserService;

import com.company.erp.erp.entites.Company;
import com.company.erp.erp.entites.Role;
import com.company.erp.erp.entites.User;

import com.company.erp.erp.entites.request.UserCreateRequest;
import com.company.erp.erp.entites.request.UserUpdateRequest;
import com.company.erp.erp.entites.response.UserResponse;
import com.company.erp.erp.mapper.UserMapper;
//import com.company.userService.UserService.jwt.TenantValidator;
import com.company.userService.repository.RoleRepository;
import com.company.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Create new user
    public UserResponse createUser(UserCreateRequest dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        User user = userMapper.fromCreateRequest(dto, role, Company.builder().build());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    // Update user
    public UserResponse updateUser(Long userId, UserUpdateRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getIsActive() != null) user.setIsActive(dto.getIsActive());

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            user.setRole(role);
        }

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    // Get all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Get user by ID
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toResponse(user);
    }

    // Delete user
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
