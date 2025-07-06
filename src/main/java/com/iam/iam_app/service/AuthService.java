package com.iam.iam_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iam.iam_app.dto.AuthResponse;
import com.iam.iam_app.dto.CreateUserRequest;
import com.iam.iam_app.dto.LoginRequest;
import com.iam.iam_app.entity.JwtToken;
import com.iam.iam_app.entity.Permission;
import com.iam.iam_app.entity.Role;
import com.iam.iam_app.entity.User;
import com.iam.iam_app.enums.RoleType;
import com.iam.iam_app.repositories.JWTRepository;
import com.iam.iam_app.repositories.PermissionRepository;
import com.iam.iam_app.repositories.RoleRepository;
import com.iam.iam_app.repositories.UserRepository;

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

        public AuthResponse register(CreateUserRequest request) {
                Role customerRole = roleRepository.findByRole(RoleType.CUSTOMER)
                                .orElseGet(() -> {
                                        Role newRole = new Role();
                                        newRole.setRole(RoleType.CUSTOMER);
                                        return roleRepository.save(newRole);
                                });

                Permission permission = new Permission();
                permission.setRead(true);
                permission.setUpdate(false);
                permission.setDelete(false);
                permission.setWrite(false);
                permissionRepository.save(permission);

                User user = new User();

                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPasswordHash(passwordEncoder.encode(request.getPassword()).toString());
                user.setAdmin(false); // or based on role
                user.setUserRole(customerRole);
                user.setPermission(permission);

                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                JwtToken jwtToken = JwtToken.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .user(user)
                                .build();
                jwtRepository.save(jwtToken);

                user.setJwtToken(jwtToken);
                userRepository.save(user);

                return new AuthResponse(
                                user.getUsername(),
                                user.getEmail(),
                                user.getUserRole().getRole().name(),
                                user.getPermission().isRead(),
                                user.getPermission().isWrite(),
                                user.getPermission().isUpdate(),
                                user.getPermission().isDelete(),
                                accessToken,
                                refreshToken);
        }

        public AuthResponse login(LoginRequest request) {
                User user = userRepository
                                .findByEmailOrUsername(request.getEmailOrUsername(), request.getEmailOrUsername())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                        throw new RuntimeException("Invalid credentials");
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
                userRepository.save(user);

                return new AuthResponse(
                                user.getUsername(),
                                user.getEmail(),
                                user.getUserRole().getRole().name(),
                                user.getPermission().isRead(),
                                user.getPermission().isWrite(),
                                user.getPermission().isUpdate(),
                                user.getPermission().isDelete(),
                                accessToken,
                                refreshToken);

        }

}
