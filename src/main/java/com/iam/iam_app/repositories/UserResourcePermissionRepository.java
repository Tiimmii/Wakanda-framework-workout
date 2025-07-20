package com.iam.iam_app.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.wakanda.framework.repository.BaseRepository;

import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.entity.UserResourcePermission;

@Repository
public interface UserResourcePermissionRepository extends BaseRepository<UserResourcePermission, Integer> {
    Optional<UserResourcePermission> findByUserAndResource(User user, Resource resource);
}
