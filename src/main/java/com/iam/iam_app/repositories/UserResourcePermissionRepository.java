package com.iam.iam_app.repositories;

import org.springframework.stereotype.Repository;
import org.wakanda.framework.repository.BaseRepository;

import com.iam.iam_app.entity.UserResourcePermission;

@Repository
public interface UserResourcePermissionRepository extends BaseRepository<UserResourcePermission, Integer> {
    
}
