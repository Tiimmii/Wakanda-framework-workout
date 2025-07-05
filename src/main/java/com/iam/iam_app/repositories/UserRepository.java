package com.iam.iam_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import com.iam.iam_app.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :value OR u.username = :value")
    Optional<User> findByEmailOrUsername(@Param("value") String value);
}
