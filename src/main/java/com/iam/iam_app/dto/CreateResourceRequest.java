package com.iam.iam_app.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateResourceRequest {
    @NotBlank(message = "Resource name cannot be blank")
    private String name;

    @NotBlank(message = "Resource type cannot be blank")
    private String type;

    @NotBlank(message = "Resource URL cannot be blank")
    private String url;
}
