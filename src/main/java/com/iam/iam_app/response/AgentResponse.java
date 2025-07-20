package com.iam.iam_app.response;

import com.iam.iam_app.dto.UserResourcePermissionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AgentResponse {
    private Integer id;
    private String username;
    private String email;
    private String role;
    private List<UserResourcePermissionDto> permissions;
}
