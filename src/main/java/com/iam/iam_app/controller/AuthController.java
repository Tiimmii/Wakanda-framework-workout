package com.iam.iam_app.controller;

import com.iam.iam_app.dto.AuthResponse;
import com.iam.iam_app.dto.CreateUserRequest;
import com.iam.iam_app.dto.LoginRequest;
import com.iam.iam_app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
