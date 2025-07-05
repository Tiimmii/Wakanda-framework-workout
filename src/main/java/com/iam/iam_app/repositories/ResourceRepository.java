package com.iam.iam_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.User;

public interface ResourceRepository extends JpaRepository<Resource, String> {
    List<Resource> findByOwner(User owner);
}
