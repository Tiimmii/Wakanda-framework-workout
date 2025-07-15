package com.iam.iam_app.service;

import java.util.List;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.response.AgentResponse;

public interface AgentService {
    AgentResponse register(CreateAgentRequest request);
    public List<AgentResponse> getAllAgents();
}
