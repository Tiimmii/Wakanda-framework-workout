package com.iam.iam_app.dto;

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
    private boolean canRead;
    private boolean canWrite;
    private boolean canUpdate;
    private boolean canDelete;
    private String accessToken;
    private String refreshToken;
}
