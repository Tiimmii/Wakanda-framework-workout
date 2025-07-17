package com.iam.iam_app.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.entity.Permission;
import com.iam.iam_app.entity.Role;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.enums.RoleType;
import com.iam.iam_app.repositories.PermissionRepository;
import com.iam.iam_app.repositories.RoleRepository;
import com.iam.iam_app.repositories.UserRepository;
import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.service.AdminService;

public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public AgentResponse register(CreateAgentRequest request) {
        SecurityContextHolder.getContext().setAuthentication(null);

        RoleType roleType = request.getRoleType();

        Role role = roleRepository.findByRole(roleType)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole(roleType);
                    return roleRepository.save(newRole);
                });

        Permission permission = new Permission();
        if (roleType == RoleType.ADMIN) {
            permission.setRead(true);
            permission.setCanUpdate(true);
            permission.setCanDelete(true);
            permission.setWrite(true);
        } else if (roleType == RoleType.CUSTOMER) {
            permission.setRead(true);
            permission.setCanUpdate(false);
            permission.setCanDelete(false);
            permission.setWrite(false);
        } else {
            // AGENT must provide permission flags
            permission.setRead(true);
            permission.setCanUpdate(request.isCanUpdate());
            permission.setCanDelete(request.isCanDelete());
            permission.setWrite(request.isCanWrite());
        }

        permissionRepository.save(permission);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAdmin(request.getRoleType() == RoleType.ADMIN ? true : false);
        user.setUserRole(role);
        user.setPermission(permission);

        userRepository.save(user);

        // ✅ Set a temporary context for @PrePersist to work
        // UserPrincipal userPrincipal = new UserPrincipal(user);
        // UsernamePasswordAuthenticationToken authToken = new
        // UsernamePasswordAuthenticationToken(userPrincipal,
        // null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(null);

        userRepository.save(user); // <-- triggers @PrePersist

        return new AgentResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole().getRole().name(),
                user.getPermission().isRead(),
                user.getPermission().isWrite(),
                user.getPermission().isCanUpdate(),
                user.getPermission().isCanDelete());
    }

    @Override
    public List<AgentResponse> getAllAgents() {
        List<User> agents = userRepository.findAllByUserRole_Role(RoleType.AGENT);

        return agents.stream().map(user -> new AgentResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole().getRole().name(),
                user.getPermission().isRead(),
                user.getPermission().isWrite(),
                user.getPermission().isCanUpdate(),
                user.getPermission().isCanDelete())).collect(Collectors.toList());
    }

    @Override
    public List<AgentResponse> getAllCustomers() {
        List<User> customers = userRepository.findAllByUserRole_Role(RoleType.CUSTOMER);

        return customers.stream().map(user -> new AgentResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole().getRole().name(),
                user.getPermission().isRead(),
                user.getPermission().isWrite(),
                user.getPermission().isCanUpdate(),
                user.getPermission().isCanDelete())).collect(Collectors.toList());
    }

    @Override
    public List<AgentResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new AgentResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getUserRole().getRole().name(),
                        user.getPermission() != null && user.getPermission().isRead(),
                        user.getPermission() != null && user.getPermission().isWrite(),
                        user.getPermission() != null && user.getPermission().isCanUpdate(),
                        user.getPermission() != null && user.getPermission().isCanDelete()))
                .collect(Collectors.toList());
    }

}
