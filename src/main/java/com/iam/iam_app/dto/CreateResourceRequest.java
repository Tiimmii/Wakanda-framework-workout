// CreateResourceRequest.java
package com.iam.iam_app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateResourceRequest {

    @NotBlank(message = "Resource name is required")
    private String name;

    @NotBlank(message = "Resource type is required")
    private String type;

    @NotBlank(message = "Resource URL is required")
    private String url;

    @NotNull(message = "Owner email is required")
    private String ownerEmail;
}
