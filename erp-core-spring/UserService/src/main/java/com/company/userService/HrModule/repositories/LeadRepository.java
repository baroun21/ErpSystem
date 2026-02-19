package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    @Query("SELECT l FROM Lead l WHERE l.companyId = :companyId AND l.status = :status ORDER BY l.createdAt DESC")
    List<Lead> findByCompanyAndStatus(@Param("companyId") Long companyId, @Param("status") String status);

    @Query("SELECT l FROM Lead l WHERE l.companyId = :companyId AND l.source = :source ORDER BY l.estimatedValue DESC")
    List<Lead> findByCompanyAndSource(@Param("companyId") Long companyId, @Param("source") String source);

    @Query("SELECT l FROM Lead l WHERE l.companyId = :companyId AND l.assignedTo = :assignedTo ORDER BY l.createdAt DESC")
    List<Lead> findByCompanyAndAssignedTo(@Param("companyId") Long companyId, @Param("assignedTo") Long assignedTo);

    @Query("SELECT l FROM Lead l WHERE l.companyId = :companyId AND l.createdAt BETWEEN :startDate AND :endDate ORDER BY l.createdAt DESC")
    List<Lead> findByCompanyAndDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT l FROM Lead l WHERE l.companyId = :companyId AND l.estimatedValue >= :minValue ORDER BY l.estimatedValue DESC")
    List<Lead> findHighValueLeads(@Param("companyId") Long companyId, @Param("minValue") java.math.BigDecimal minValue);

    @Query("SELECT l FROM Lead l WHERE l.companyId = :companyId AND l.email LIKE CONCAT('%', :email, '%')")
    List<Lead> findByEmailContaining(@Param("companyId") Long companyId, @Param("email") String email);

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.companyId = :companyId AND l.status = :status")
    Long countByStatus(@Param("companyId") Long companyId, @Param("status") String status);
}
