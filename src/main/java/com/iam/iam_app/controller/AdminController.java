package com.iam.iam_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/create-user")
    public ResponseEntity<?> register(@RequestBody CreateAgentRequest request) {
        AgentResponse response = adminService.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agents")
    public ResponseEntity<List<AgentResponse>> getAllAgents() {
        List<AgentResponse> agents = adminService.getAllAgents();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<AgentResponse>> getAllCustomers() {
        List<AgentResponse> customers = adminService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AgentResponse>> getAllUsers() {
        List<AgentResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
