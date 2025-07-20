package com.iam.iam_app.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wakanda.framework.exception.BaseException;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.dto.CreateResourceRequest;
import com.iam.iam_app.entity.Permission;
import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.Role;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.enums.RoleType;
import com.iam.iam_app.repositories.PermissionRepository;
import com.iam.iam_app.repositories.ResourceRepository;
import com.iam.iam_app.repositories.RoleRepository;
import com.iam.iam_app.repositories.UserRepository;
import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.service.AgentService;

@Service
public class AgentServiceImpl implements AgentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public void createResource(CreateResourceRequest request) {
        User owner = userRepository.findByEmail(request.getOwnerEmail())
                .orElseThrow(() -> new BaseException(404, "User with email " + request.getOwnerEmail() + " not found"));

        Resource resource = Resource.builder()
                .name(request.getName())
                .type(request.getType())
                .url(request.getUrl())
                .owner(owner)
                .build();
        SecurityContextHolder.getContext().setAuthentication(null);
        resourceRepository.save(resource);
    }

}
