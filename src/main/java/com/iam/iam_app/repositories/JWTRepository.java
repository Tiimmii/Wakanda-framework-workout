package com.iam.iam_app.repositories;

import org.wakanda.framework.repository.BaseRepository;

import com.iam.iam_app.entity.JwtToken;
import com.iam.iam_app.entity.User;

public interface JWTRepository extends BaseRepository<JwtToken, Integer> {
    void deleteByUser(User user);
}
