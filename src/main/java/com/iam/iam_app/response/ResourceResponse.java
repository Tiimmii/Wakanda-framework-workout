package com.iam.iam_app.response;

import com.iam.iam_app.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResourceResponse {
    private String name;
    private String type;
    private String url;
    // private String owner;
}
