package com.iam.iam_app.repositories;

import org.springframework.stereotype.Repository;
import org.wakanda.framework.repository.BaseRepository;

import java.util.Optional;
import com.iam.iam_app.entity.User;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUsername(String email, String username);
}
