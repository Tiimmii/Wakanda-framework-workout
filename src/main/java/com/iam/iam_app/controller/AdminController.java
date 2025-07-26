package com.iam.iam_app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.iam.iam_app.dto.CreateAgentRequest;
import com.iam.iam_app.dto.UpdatePermissionRequest;
import com.iam.iam_app.response.AgentResponse;
import com.iam.iam_app.service.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Admin", description = "Operations related to admin functions in the IAM System")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/create-user")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    public ResponseEntity<?> register(@RequestBody CreateAgentRequest request) {
        AgentResponse response = adminService.register(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @GetMapping("/agents")
    public ResponseEntity<List<AgentResponse>> getAllAgents() {
        List<AgentResponse> agents = adminService.getAllAgents();
        return ResponseEntity.ok(agents);
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @GetMapping("/customers")
    public ResponseEntity<List<AgentResponse>> getAllCustomers() {
        List<AgentResponse> customers = adminService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<AgentResponse>> getAllUsers() {
        List<AgentResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @PatchMapping("/users/{id}")
    public ResponseEntity<AgentResponse> patchUser(
            @PathVariable Integer id,
            @RequestBody CreateAgentRequest request) {
        AgentResponse updatedUser = adminService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @PutMapping("/users/update-permissions")
    public ResponseEntity<?> updatePermission(@RequestBody @Valid UpdatePermissionRequest request) {
        adminService.updateUserPermission(request);
        return ResponseEntity.ok("Permissions updated successfully");
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        adminService.deleteUserById(id);
        return ResponseEntity.ok().body("User deleted successfully");
    }
}
