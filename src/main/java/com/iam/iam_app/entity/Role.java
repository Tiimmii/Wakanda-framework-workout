package com.iam.iam_app.entity;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.wakanda.framework.entity.BaseEntity;

import com.iam.iam_app.entity.enums.RoleType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity<Integer>{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, name = "role")
    @NotNull(message = "Role type is required")
    private RoleType role;

    // ✅ One role can be shared by many users
    @OneToMany(mappedBy = "userRole")
    private List<User> users;
}

