package com.iam.iam_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
public class User extends BaseEntity {

    @NotBlank(message = "username cannot be blank")
    @Column(name = "username", nullable = false)
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @ManyToOne
    @JoinColumn(name = "user_role")
    private Role userRole;

    public User() {
        super();
    }
}
