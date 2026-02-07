package com.company.userService.repository;

import com.company.erp.erp.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);


    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByUsernameAndCompanyId(String username, Long companyId);
}
