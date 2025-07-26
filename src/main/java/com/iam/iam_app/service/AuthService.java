package com.iam.iam_app.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wakanda.framework.exception.BaseException;
import org.wakanda.framework.model.UserPrincipal;

import com.iam.iam_app.bridge.WakandaUserAdapter;
import com.iam.iam_app.dto.CreateUserRequest;
import com.iam.iam_app.dto.LoginRequest;
import com.iam.iam_app.entity.JwtToken;
import com.iam.iam_app.entity.Permission;
import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.entity.Role;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.entity.UserResourcePermission;
import com.iam.iam_app.entity.enums.RoleType;
import com.iam.iam_app.repositories.JWTRepository;
import com.iam.iam_app.repositories.PermissionRepository;
import com.iam.iam_app.repositories.ResourceRepository;
import com.iam.iam_app.repositories.RoleRepository;
import com.iam.iam_app.repositories.UserRepository;
import com.iam.iam_app.repositories.UserResourcePermissionRepository;
import com.iam.iam_app.response.AuthResponse;

@Service
public class AuthService {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtService jwtService;

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private JWTRepository jwtRepository;

        @Autowired
        private PermissionRepository permissionRepository;

        @Autowired
        private ResourceRepository resourceRepository;

        @Autowired
        private UserResourcePermissionRepository userResourcePermissionRepository;

        public AuthResponse register(CreateUserRequest request) {
                SecurityContextHolder.getContext().setAuthentication(null);
                List<User> users = userRepository.findAll();
                Role customerRole = roleRepository.findByRole(RoleType.CUSTOMER)
                                .orElseGet(() -> {
                                        Role newRole = new Role();
                                        newRole.setRole(RoleType.CUSTOMER);
                                        return roleRepository.save(newRole);
                                });

                Role adminRole = roleRepository.findByRole(RoleType.ADMIN)
                                .orElseGet(() -> {
                                        Role newRole = new Role();
                                        newRole.setRole(RoleType.ADMIN);
                                        return roleRepository.save(newRole);
                                });

                User user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                user.setAdmin(users.isEmpty() ? true : false);
                user.setUserRole(users.isEmpty() ? adminRole : customerRole);

                userRepository.save(user);

                List<Resource> allResources = resourceRepository.findAll();

                for (Resource resource : allResources) {
                        Permission permission = new Permission();
                        if (users.isEmpty()) {
                                permission.setRead(true);
                                permission.setCanUpdate(true);
                                permission.setCanDelete(true);
                                permission.setWrite(true);
                        } else {
                                permission.setRead(false);
                                permission.setCanUpdate(false);
                                permission.setCanDelete(false);
                                permission.setWrite(false);
                        }
                        permissionRepository.save(permission);

                        UserResourcePermission urp = new UserResourcePermission();
                        urp.setUser(user);
                        urp.setResource(resource);
                        urp.setPermission(permission);

                        user.getUserResourcePermissions().add(urp);
                        userResourcePermissionRepository.save(urp);

                }

                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                JwtToken jwtToken = JwtToken.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .user(user)
                                .build();
                jwtRepository.save(jwtToken);
                user.setJwtToken(jwtToken);

                // Create a temporary UserPrincipal
                UserPrincipal tempPrincipal = new UserPrincipal(
                                new WakandaUserAdapter(user));

                // Set temporary authentication
                Authentication auth = new UsernamePasswordAuthenticationToken(
                                tempPrincipal, null, tempPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                userRepository.save(user); // <-- triggers @PrePersist

                return new AuthResponse(
                                user.getUsername(),
                                user.getEmail(),
                                user.getUserRole().getRole().name(),
                                accessToken,
                                refreshToken);
        }

        @Transactional
        public AuthResponse login(LoginRequest request) {
                SecurityContextHolder.getContext().setAuthentication(null);
                Optional<User> userOpt = userRepository.findByEmail(request.getEmailOrUsername());

                if (userOpt.isEmpty()) {
                        userOpt = userRepository.findByUsername(request.getEmailOrUsername());
                }

                User user = userOpt.orElseThrow(() -> new BaseException(401, "User not found"));

                if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                        throw new BaseException(401, "Invalid credentials");
                }

                jwtRepository.deleteByUser(user);

                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                JwtToken jwtToken = JwtToken.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .user(user)
                                .build();

                jwtRepository.save(jwtToken);

                user.setJwtToken(jwtToken);
                UserPrincipal tempPrincipal = new UserPrincipal(
                                new WakandaUserAdapter(user));

                // Set temporary authentication
                Authentication auth = new UsernamePasswordAuthenticationToken(
                                tempPrincipal, null, tempPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                userRepository.save(user);

                return new AuthResponse(
                                user.getUsername(),
                                user.getEmail(),
                                user.getUserRole().getRole().name(),
                                accessToken,
                                refreshToken);

        }

}
