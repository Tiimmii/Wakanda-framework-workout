package com.iam.iam_app.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.iam.iam_app.enums.RoleType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAgentRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private RoleType roleType; // AGENT or ADMIN

    private boolean canUpdate;
    private boolean canDelete;
    private boolean canWrite;
}
