package com.iam.iam_app.entity;
import com.iam.iam_app.enums.RoleType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    @NotNull(message = "Role type is required")
    private RoleType role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_permission", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Permissions must be assigned to role")
    private Permission rolePermission;
}
