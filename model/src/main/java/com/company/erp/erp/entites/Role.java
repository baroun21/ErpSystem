package com.company.erp.erp.entites;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(
        name = "ROLES",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ROLES_NAME_CO", columnNames = {"ROLE_NAME", "COMPANY_ID"})
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(TenantEntityListener.class)
public class Role extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "ROLE_NAME", nullable = false, length = 50)
    private String roleName;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
}