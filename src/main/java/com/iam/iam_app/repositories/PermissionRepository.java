package com.iam.iam_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iam.iam_app.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
}
