package com.company.erp.shared.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Base entity for all domain models.
 * Provides:
 *  - Multi-tenancy (company_id)
 *  - Multi-entity support (legal_entity_id)
 *  - Audit timestamps
 *  - Common ID field
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tenant identifier (company_id) - REQUIRED for all entities
     * Used for automatic multi-tenant filtering
     */
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    /**
     * Legal entity identifier (optional, for multi-entity support)
     * Allows single company to have multiple legal entities
     */
    @Column(name = "legal_entity_id")
    private Long legalEntityId;

    /**
     * Audit timestamp - automatically set on creation
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Audit timestamp - automatically updated on modification
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * User who created this record (audit)
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * User who last modified this record (audit)
     */
    @Column(name = "updated_by")
    private String updatedBy;
}

