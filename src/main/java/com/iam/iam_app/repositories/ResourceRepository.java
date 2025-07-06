package com.iam.iam_app.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.wakanda.framework.repository.BaseRepository;

import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.User;

@Repository
public interface ResourceRepository extends BaseRepository<Resource, Integer> {
    List<Resource> findByOwner(User owner);
}
