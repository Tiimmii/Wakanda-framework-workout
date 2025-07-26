package com.iam.iam_app.service;

import java.util.List;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.dto.CreateResourceRequest;
import com.iam.iam_app.response.AgentResponse;

public interface AgentService {
    void createResource(CreateResourceRequest request);
    public void deleteResourceById(Integer resourceId); 
}
