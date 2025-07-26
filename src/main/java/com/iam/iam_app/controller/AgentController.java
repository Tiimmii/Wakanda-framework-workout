package com.iam.iam_app.controller;

import com.iam.iam_app.dto.CreateResourceRequest;
import com.iam.iam_app.service.AgentService;

import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Agent", description = "Operations related to agent functions in the IAM System")
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PreAuthorize("isAuthenticated() and (hasAuthority('AGENT') or hasAuthority('ADMIN'))")
    @PostMapping("/create-resource")
    public ResponseEntity<String> createResource(@RequestBody @Valid CreateResourceRequest request) {
        agentService.createResource(request);
        return ResponseEntity.ok("Resource created successfully");
    }

}
