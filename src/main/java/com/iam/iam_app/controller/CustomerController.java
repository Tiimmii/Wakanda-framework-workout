package com.iam.iam_app.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.iam.iam_app.response.ResourceResponse;
import com.iam.iam_app.service.CustomerService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Resources", description = "Operations related to normal users functions in the IAM System")
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/resource/{id}")
    public ResponseEntity<ResourceResponse> getResource(@PathVariable Integer id){
        ResourceResponse resources = customerService.getResource(id);
        return ResponseEntity.ok(resources); 
    }
}
