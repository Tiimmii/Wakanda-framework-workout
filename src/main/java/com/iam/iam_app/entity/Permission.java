package com.iam.iam_app.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.wakanda.framework.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission extends BaseEntity<Integer> {

    @NotNull
    @Column(name = "can_read", nullable = false)
    private boolean read;

    @NotNull
    @Column(name = "can_write", nullable = false)
    private boolean write;

    @NotNull
    @Column(name = "can_delete", nullable = false)
    private boolean canDelete;  // Renamed from "delete"

    @NotNull
    @Column(name = "can_update", nullable = false)
    private boolean canUpdate;  // Renamed from "update"

    @OneToOne(mappedBy = "permission")
    private User user;
}
