package com.iam.iam_app.entity;

import javax.persistence.*;

import org.wakanda.framework.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission extends BaseEntity<Integer>{

    private boolean read;
    private boolean write;
    private boolean delete;
    private boolean update;

    @OneToOne(mappedBy = "permission")
    private User user;
}

