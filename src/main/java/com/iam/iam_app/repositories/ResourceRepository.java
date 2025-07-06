package com.iam.iam_app.repositories;

import java.util.List;
import org.wakanda.framework.repository.BaseRepository;

import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.User;

public interface ResourceRepository extends BaseRepository<Resource, Integer> {
    List<Resource> findByOwner(User owner);
}
