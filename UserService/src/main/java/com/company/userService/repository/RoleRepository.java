package com.company.userService.repository;

import com.company.erp.erp.entites.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleNameAndCompanyId(String roleName , Long companyId);
}
