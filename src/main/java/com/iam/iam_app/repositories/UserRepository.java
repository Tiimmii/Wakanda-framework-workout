package com.iam.iam_app.repositories;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wakanda.framework.repository.BaseRepository;

import java.util.Optional;
import com.iam.iam_app.entity.User;

public interface UserRepository extends BaseRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :value OR u.username = :value")
    Optional<User> findByEmailOrUsername(@Param("value") String value);
}
