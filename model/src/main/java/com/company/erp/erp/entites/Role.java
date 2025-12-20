package com.company.erp.erp.entites;


import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROLES")
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "ROLE_NAME", nullable = false, unique = true, length = 50)
    private String roleName;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    // One Role → Many Users
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users = new HashSet<>();

    public Role(Long roleId, String roleName, String description, Set<User> users) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.users = users;
    }
}
