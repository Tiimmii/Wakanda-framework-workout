package com.iam.iam_app.entity;

import java.util.List;

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
    @Column(nullable = false, unique = true, name = "role")
    @NotNull(message = "Role type is required")
    private RoleType role;

    // ✅ One role can be shared by many users
    @OneToMany(mappedBy = "userRole")
    private List<User> users;
}

