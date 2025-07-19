package com.iam.iam_app.service;

import java.util.List;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.response.AgentResponse;

public interface AdminService {
    AgentResponse register(CreateAgentRequest request);
    public List<AgentResponse> getAllAgents();
    public List<AgentResponse> getAllCustomers();
    public List<AgentResponse> getAllUsers();
    void deleteUserById(Integer userId);
}
