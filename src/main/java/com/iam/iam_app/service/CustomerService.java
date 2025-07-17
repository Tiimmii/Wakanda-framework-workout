package com.iam.iam_app.service;

import java.util.List;

import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.response.ResourceResponse;

public interface CustomerService {
    public List<ResourceResponse> getAllResource();
}
