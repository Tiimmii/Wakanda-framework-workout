package com.iam.iam_app.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.wakanda.framework.repository.BaseRepository;

import com.iam.iam_app.entity.Role;
import com.iam.iam_app.entity.enums.RoleType;

@Repository
public interface RoleRepository extends BaseRepository<Role, Integer> {
    Optional<Role> findByRole(RoleType role);
}
