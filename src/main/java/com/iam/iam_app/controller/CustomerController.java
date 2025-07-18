package com.iam.iam_app.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.iam.iam_app.response.ResourceResponse;
import com.iam.iam_app.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/resource")
    public ResponseEntity<List<ResourceResponse>> getAllResource(){
        List<ResourceResponse> resources = customerService.getAllResource();
        return ResponseEntity.ok(resources); 
    }
}
