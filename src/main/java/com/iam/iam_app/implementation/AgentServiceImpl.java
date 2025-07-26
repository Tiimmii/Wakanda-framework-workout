package com.iam.iam_app.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.wakanda.framework.exception.BaseException;
import org.wakanda.framework.model.UserPrincipal;

import com.iam.iam_app.bridge.AuthHandler;
import com.iam.iam_app.dto.CreateResourceRequest;
import com.iam.iam_app.entity.Permission;
import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.entity.UserResourcePermission;
import com.iam.iam_app.repositories.PermissionRepository;
import com.iam.iam_app.repositories.ResourceRepository;
import com.iam.iam_app.repositories.UserRepository;
import com.iam.iam_app.repositories.UserResourcePermissionRepository;
import com.iam.iam_app.service.AgentService;

@Service
public class AgentServiceImpl implements AgentService {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ResourceRepository resourceRepository;

        @Autowired
        private PermissionRepository permissionRepository;

        @Autowired
        private UserResourcePermissionRepository userResourcePermissionRepository;

        @Override
        public void createResource(CreateResourceRequest request) {
                UserPrincipal principal = new AuthHandler().getAuthenticatedUser();
                User owner = userRepository.findById(Integer.parseInt(principal.getUser().getUserId()))
                                .orElseThrow(() -> new BaseException(404, "Authenticated user not found"));

                Resource resource = Resource.builder()
                                .name(request.getName())
                                .type(request.getType())
                                .url(request.getUrl())
                                .owner(owner)
                                .build();
                resourceRepository.save(resource);

                Permission permission = new Permission();
                permission.setRead(true);
                permission.setWrite(true);
                permission.setCanUpdate(true);
                permission.setCanDelete(true);
                permissionRepository.save(permission);

                UserResourcePermission newUrp = new UserResourcePermission();
                newUrp.setUser(owner);
                newUrp.setResource(resource);
                newUrp.setPermission(permission);

                userResourcePermissionRepository.save(newUrp);

        }

        @Override
        public void deleteResourceById(Integer resourceId) {
                UserPrincipal principal = new AuthHandler().getAuthenticatedUser();
                User user = userRepository.findById(Integer.parseInt(principal.getUser().getUserId()))
                                .orElseThrow(() -> new BaseException(404, "Authenticated user not found"));
                Resource resource = resourceRepository.findById(resourceId)
                                .orElseThrow(() -> new BaseException(404, "Resource not found"));
                if (user.isAdmin()) {
                        resourceRepository.delete(resource);
                        return;
                }

                UserResourcePermission urp = userResourcePermissionRepository
                                .findByUserAndResource(user, resource)
                                .orElseThrow(() -> new BaseException(404,
                                                "You are not permitted to delete this resource"));

                if (!urp.getPermission().isCanDelete()) {
                        throw new BaseException(401, "You are not permitted to delete this resource");
                }
                resourceRepository.delete(resource);

        }

        @Override
        public void updateResource(Integer resourceId, CreateResourceRequest request) {
                UserPrincipal principal = new AuthHandler().getAuthenticatedUser();
                User user = userRepository.findById(Integer.parseInt(principal.getUser().getUserId()))
                                .orElseThrow(() -> new BaseException(404, "Authenticated user not found"));
                Resource resource = resourceRepository.findById(resourceId)
                                .orElseThrow(() -> new BaseException(404, "Resource not found"));
                if (user.isAdmin()) {
                        if (request.getName() != null) {
                                resource.setName(request.getName());
                        }

                        if (request.getUrl() != null) {
                                resource.setUrl(request.getUrl());
                        }

                        if (request.getType() != null) {
                                resource.setType(request.getType());
                        }
                        return;
                }

                UserResourcePermission urp = userResourcePermissionRepository
                                .findByUserAndResource(user, resource)
                                .orElseThrow(() -> new BaseException(404,
                                                "You are not permitted to delete this resource"));
                if (!urp.getPermission().isCanUpdate()) {
                        throw new BaseException(401, "You are not permitted to delete this resource");
                }
                if (request.getName() != null) {
                        resource.setName(request.getName());
                }

                if (request.getUrl() != null) {
                        resource.setUrl(request.getUrl());
                }

                if (request.getType() != null) {
                        resource.setType(request.getType());
                }
                resourceRepository.save(resource);
        }

}
