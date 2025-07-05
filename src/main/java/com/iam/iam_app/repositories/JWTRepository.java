package com.iam.iam_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iam.iam_app.entity.JwtToken;
import com.iam.iam_app.entity.User;

public interface JWTRepository extends JpaRepository<JwtToken, String> {
    void deleteByUser(User user);
}
