package com.iam.iam_app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String username;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
}
