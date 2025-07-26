package com.iam.iam_app.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wakanda.framework.exception.BaseException;
import org.wakanda.framework.model.UserPrincipal;

import com.iam.iam_app.bridge.AuthHandler;
import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.entity.UserResourcePermission;
import com.iam.iam_app.repositories.PermissionRepository;
import com.iam.iam_app.repositories.ResourceRepository;
import com.iam.iam_app.repositories.UserRepository;
import com.iam.iam_app.repositories.UserResourcePermissionRepository;
import com.iam.iam_app.response.ResourceResponse;
import com.iam.iam_app.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserResourcePermissionRepository userResourcePermissionRepository;

    public ResourceResponse getResource(Integer id) {
        UserPrincipal principal = new AuthHandler().getAuthenticatedUser();
        User user = userRepository.findById(Integer.parseInt(principal.getUser().getUserId()))
                .orElseThrow(() -> new BaseException(404, "Authenticated user not found"));
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new BaseException(404, "Resource not found"));

        if (user.isAdmin()) {
            return new ResourceResponse(
                    resource.getName(),
                    resource.getType(),
                    resource.getUrl(),
                    resource.getOwner().getUsername().toString());
        }

        UserResourcePermission urp = userResourcePermissionRepository
                .findByUserAndResource(user, resource)
                .orElseThrow(() -> new BaseException(404,
                        "You are not permitted to access this resource"));
        
        if(!urp.getPermission().isRead()){
            throw new BaseException(401, "You are not permitted to access this resource");
        }

        return new ResourceResponse(
                    resource.getName(),
                    resource.getType(),
                    resource.getUrl(),
                    resource.getOwner().getUsername().toString());

    }
}
