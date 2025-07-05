package com.iam.iam_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String emailOrUsername;
    private String password;
}
