package com.iam.iam_app.repositories;

import org.springframework.stereotype.Repository;
import org.wakanda.framework.repository.BaseRepository;

import com.iam.iam_app.entity.Permission;

@Repository
public interface PermissionRepository extends BaseRepository<Permission, Integer> {
    
}
