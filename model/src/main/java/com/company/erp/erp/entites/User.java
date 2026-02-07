package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"role"})
@EqualsAndHashCode(exclude = {"role"})
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "FULL_NAME", length = 100)
    private String fullName;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Employee employee;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PasswordResetToken passwordResetToken;
}
