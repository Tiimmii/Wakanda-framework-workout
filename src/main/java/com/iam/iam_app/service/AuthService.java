package com.iam.iam_app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
import com.iam.iam_app.response.AuthResponse;
import com.iam.iam_app.security.UserPrincipal;

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
                SecurityContextHolder.getContext().setAuthentication(null);
                Role customerRole = roleRepository.findByRole(RoleType.CUSTOMER)
                                .orElseGet(() -> {
                                        Role newRole = new Role();
                                        newRole.setRole(RoleType.CUSTOMER);
                                        return roleRepository.save(newRole);
                                });
                SecurityContextHolder.clearContext();
                Permission permission = new Permission();
                permission.setRead(true);
                permission.setCanUpdate(false);
                permission.setCanDelete(false);
                permission.setWrite(false);
                permissionRepository.save(permission);

                User user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                user.setAdmin(false);
                user.setUserRole(customerRole);
                user.setPermission(permission);

                userRepository.save(user);

                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                JwtToken jwtToken = JwtToken.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .user(user)
                                .build();
                jwtRepository.save(jwtToken);
                user.setJwtToken(jwtToken);

                // ✅ Set a temporary context for @PrePersist to work
                UserPrincipal userPrincipal = new UserPrincipal(user);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userPrincipal,
                                null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(null);

                userRepository.save(user); // <-- triggers @PrePersist

                return new AuthResponse(
                                user.getUsername(),
                                user.getEmail(),
                                user.getUserRole().getRole().name(),
                                user.getPermission().isRead(),
                                user.getPermission().isWrite(),
                                user.getPermission().isCanUpdate(),
                                user.getPermission().isCanDelete(),
                                accessToken,
                                refreshToken);
        }

        public AuthResponse login(LoginRequest request) {
                SecurityContextHolder.getContext().setAuthentication(null);
                Optional<User> userOpt = userRepository.findByEmail(request.getEmailOrUsername());

                if (userOpt.isEmpty()) {
                        userOpt = userRepository.findByUsername(request.getEmailOrUsername());
                }

                User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));

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
                // ✅ Set authenticated principal for BaseEntity to access
                // UserPrincipal userPrincipal = new UserPrincipal(user);
                // Authentication auth = new UsernamePasswordAuthenticationToken(
                // userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(null);
                userRepository.save(user);

                return new AuthResponse(
                                user.getUsername(),
                                user.getEmail(),
                                user.getUserRole().getRole().name(),
                                user.getPermission().isRead(),
                                user.getPermission().isWrite(),
                                user.getPermission().isCanUpdate(),
                                user.getPermission().isCanDelete(),
                                accessToken,
                                refreshToken);

        }

}
