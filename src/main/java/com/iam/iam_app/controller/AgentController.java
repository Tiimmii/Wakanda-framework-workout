package com.iam.iam_app.controller;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.dto.CreateResourceRequest;
import com.iam.iam_app.dto.CreateUserRequest;
import com.iam.iam_app.dto.LoginRequest;
import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.service.AgentService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Agent", description = "Operations related to agent functions in the IAM System")
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping("/create-resource")
    public ResponseEntity<String> createResource(@RequestBody @Valid CreateResourceRequest request) {
        agentService.createResource(request);
        return ResponseEntity.ok("Resource created successfully");
    }

}
