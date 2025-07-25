package com.iam.iam_app.service;

import java.util.List;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.dto.UpdateAgentRequest;
import com.iam.iam_app.dto.UpdatePermissionRequest;
import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.response.ResourceResponse;

public interface AdminService {
    AgentResponse register(CreateAgentRequest request);
    public List<AgentResponse> getAllAgents();
    public List<AgentResponse> getAllCustomers();
    public List<AgentResponse> getAllUsers();
    AgentResponse updateUser(Integer userId, UpdateAgentRequest request);
    void deleteUserById(Integer userId);
    public void updateUserPermission(UpdatePermissionRequest request);
    public List<ResourceResponse> getAllResource(); 
}
