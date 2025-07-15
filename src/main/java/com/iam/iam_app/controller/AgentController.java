package com.iam.iam_app.controller;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.dto.CreateUserRequest;
import com.iam.iam_app.dto.LoginRequest;
import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.service.AgentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping("/create-agent-admin")
    public ResponseEntity<?> register(@RequestBody CreateAgentRequest request) {
        AgentResponse response = agentService.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agents")
    public ResponseEntity<List<AgentResponse>> getAllAgents() {
        List<AgentResponse> agents = agentService.getAllAgents();
        return ResponseEntity.ok(agents);
    }

}
