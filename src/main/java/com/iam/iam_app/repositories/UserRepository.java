package com.iam.iam_app.repositories;

import org.springframework.stereotype.Repository;
import org.wakanda.framework.repository.BaseRepository;

import java.util.List;
import java.util.Optional;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.enums.RoleType;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    List<User> findAllByUserRole_Role(RoleType roleType);
}
