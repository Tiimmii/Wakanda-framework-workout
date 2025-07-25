package com.iam.iam_app.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wakanda.framework.exception.BaseException;
import org.wakanda.framework.model.UserPrincipal;

import com.iam.iam_app.bridge.WakandaUserAdapter;
import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.dto.UpdateAgentRequest;
import com.iam.iam_app.dto.UpdatePermissionRequest;
import com.iam.iam_app.dto.UserResourcePermissionDto;
import com.iam.iam_app.entity.Permission;
import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.Role;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.entity.UserResourcePermission;
import com.iam.iam_app.entity.enums.RoleType;
import com.iam.iam_app.repositories.PermissionRepository;
import com.iam.iam_app.repositories.ResourceRepository;
import com.iam.iam_app.repositories.RoleRepository;
import com.iam.iam_app.repositories.UserRepository;
import com.iam.iam_app.repositories.UserResourcePermissionRepository;
import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.response.ResourceResponse;
import com.iam.iam_app.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserResourcePermissionRepository userResourcePermissionRepository;

    @Override
    public AgentResponse register(CreateAgentRequest request) {
        RoleType roleType = request.getRoleType();

        Role role = roleRepository.findByRole(roleType)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole(roleType);
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAdmin(request.getRoleType() == RoleType.ADMIN ? true : false);
        user.setUserRole(role);

        userRepository.save(user);

        List<Resource> resources = resourceRepository.findAll(); // Or filter specific ones

        for (Resource resource : resources) {
            Permission permission = new Permission();

            if (roleType == RoleType.ADMIN) {
                permission.setRead(true);
                permission.setCanUpdate(true);
                permission.setCanDelete(true);
                permission.setWrite(true);
            } else if (roleType == RoleType.CUSTOMER) {
                permission.setRead(true);
            } else {
                permission.setRead(true);
                permission.setCanUpdate(request.isCanUpdate());
                permission.setCanDelete(request.isCanDelete());
                permission.setWrite(request.isCanWrite());
            }

            permissionRepository.save(permission);

            UserResourcePermission urp = UserResourcePermission.builder()
                    .user(user)
                    .resource(resource)
                    .permission(permission)
                    .build();

            user.getUserResourcePermissions().add(urp);
            userResourcePermissionRepository.save(urp);
        }

        userRepository.save(user); // <-- triggers @PrePersist

        List<UserResourcePermissionDto> permissionDtos = user.getUserResourcePermissions()
                .stream()
                .map(urp -> new UserResourcePermissionDto(
                        urp.getResource().getName(),
                        urp.getResource().getType(),
                        urp.getResource().getUrl(),
                        urp.getPermission().isRead(),
                        urp.getPermission().isWrite(),
                        urp.getPermission().isCanUpdate(),
                        urp.getPermission().isCanDelete()))
                .toList();

        return new AgentResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole().getRole().name(),
                permissionDtos);
    }

    @Override
    public List<AgentResponse> getAllAgents() {
        List<User> agents = userRepository.findAllByUserRole_Role(RoleType.AGENT);

        return agents.stream().map(user -> {
            List<UserResourcePermissionDto> permissions = user.getUserResourcePermissions()
                    .stream()
                    .map(urp -> new UserResourcePermissionDto(
                            urp.getResource().getName(),
                            urp.getResource().getType(),
                            urp.getResource().getUrl(),
                            urp.getPermission().isRead(),
                            urp.getPermission().isWrite(),
                            urp.getPermission().isCanUpdate(),
                            urp.getPermission().isCanDelete()))
                    .collect(Collectors.toList());

            return new AgentResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getUserRole().getRole().name(),
                    permissions);
        }).collect(Collectors.toList());
    }

    @Override
    public List<AgentResponse> getAllCustomers() {
        List<User> customers = userRepository.findAllByUserRole_Role(RoleType.CUSTOMER);

        return customers.stream().map(user -> {
            List<UserResourcePermissionDto> permissions = user.getUserResourcePermissions()
                    .stream()
                    .map(urp -> new UserResourcePermissionDto(
                            urp.getResource().getName(),
                            urp.getResource().getType(),
                            urp.getResource().getUrl(),
                            urp.getPermission().isRead(),
                            urp.getPermission().isWrite(),
                            urp.getPermission().isCanUpdate(),
                            urp.getPermission().isCanDelete()))
                    .collect(Collectors.toList());

            return new AgentResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getUserRole().getRole().name(),
                    permissions);
        }).collect(Collectors.toList());
    }

    @Override
    public List<AgentResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            List<UserResourcePermissionDto> permissions = user.getUserResourcePermissions()
                    .stream()
                    .map(urp -> new UserResourcePermissionDto(
                            urp.getResource().getName(),
                            urp.getResource().getType(),
                            urp.getResource().getUrl(),
                            urp.getPermission().isRead(),
                            urp.getPermission().isWrite(),
                            urp.getPermission().isCanUpdate(),
                            urp.getPermission().isCanDelete()))
                    .collect(Collectors.toList());

            return new AgentResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getUserRole().getRole().name(),
                    permissions);
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new BaseException(401, "User not authenticated");
        }
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        if (principal.getUser().getUserId().equals(userId.toString())) {
            throw new BaseException(405, "You cannot delete yourself");
        }

        userRepository.delete(user);
    }

    @Override
    public AgentResponse updateUser(Integer userId, UpdateAgentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoleType() != null) {
            Role role = roleRepository.findByRole(request.getRoleType())
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRole(request.getRoleType());
                        return roleRepository.save(newRole);
                    });
            user.setUserRole(role);
        }

        // Update each UserResourcePermission
        // for (UserResourcePermission urp : user.getUserResourcePermissions()) {
        // Permission permission = urp.getPermission();

        // if (request.getRoleType() == RoleType.ADMIN) {
        // permission.setWrite(true);
        // permission.setRead(true);
        // permission.setCanUpdate(true);
        // permission.setCanDelete(true);
        // } else if (request.getRoleType() == RoleType.AGENT) {
        // permission.setRead(true);
        // permission.setWrite(request.isCanWrite());
        // permission.setCanUpdate(request.isCanUpdate());
        // permission.setCanDelete(request.isCanDelete());
        // } else {
        // // CUSTOMER
        // permission.setRead(true);
        // permission.setWrite(false);
        // permission.setCanUpdate(false);
        // permission.setCanDelete(false);
        // }

        // permissionRepository.save(permission);
        // }

        userRepository.save(user);

        List<UserResourcePermissionDto> permissionDtos = user.getUserResourcePermissions()
                .stream()
                .map(urp -> new UserResourcePermissionDto(
                        urp.getResource().getName(),
                        urp.getResource().getType(),
                        urp.getResource().getUrl(),
                        urp.getPermission().isRead(),
                        urp.getPermission().isWrite(),
                        urp.getPermission().isCanUpdate(),
                        urp.getPermission().isCanDelete()))
                .collect(Collectors.toList());

        return new AgentResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole().getRole().name(),
                permissionDtos);
    }

    @Override
    public void updateUserPermission(UpdatePermissionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BaseException(404, "User not found"));

        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new BaseException(404, "Resource not found"));

        Optional<UserResourcePermission> optionalUrp = userResourcePermissionRepository.findByUserAndResource(user,
                resource);

        Permission permission;

        if (optionalUrp.isEmpty()) {
            permission = new Permission();
            permission.setRead(request.isCanRead());
            permission.setWrite(request.isCanWrite());
            permission.setCanUpdate(request.isCanUpdate());
            permission.setCanDelete(request.isCanDelete());

            permission = permissionRepository.save(permission);

            UserResourcePermission newUrp = new UserResourcePermission();
            newUrp.setUser(user);
            newUrp.setResource(resource);
            newUrp.setPermission(permission);

            userResourcePermissionRepository.save(newUrp);
        } else {
            UserResourcePermission urp = optionalUrp.get();
            permission = urp.getPermission();

            permission.setRead(request.isCanRead());
            permission.setWrite(request.isCanWrite());
            permission.setCanUpdate(request.isCanUpdate());
            permission.setCanDelete(request.isCanDelete());

            permissionRepository.save(permission);
        }
    }

    @Override
    public List<ResourceResponse> getAllResource() {
        List<Resource> resources = resourceRepository.findAll();

        return resources.stream()
                .map(resource -> new ResourceResponse(
                        resource.getName(),
                        resource.getType(),
                        resource.getUrl(),
                        resource.getOwner().getUsername().toString()))
                .collect(Collectors.toList());
    }
}
