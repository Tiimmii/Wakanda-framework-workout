package com.iam.iam_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iam.iam_app.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
