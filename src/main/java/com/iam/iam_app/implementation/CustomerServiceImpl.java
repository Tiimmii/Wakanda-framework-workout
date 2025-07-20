package com.iam.iam_app.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iam.iam_app.entity.Resource;
import com.iam.iam_app.repositories.ResourceRepository;
import com.iam.iam_app.response.ResourceResponse;
import com.iam.iam_app.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private ResourceRepository resourceRepository;

    public List<ResourceResponse> getAllResource(){
        List<Resource> resources = resourceRepository.findAll();

        return resources.stream()
                .map(resource -> new ResourceResponse(
                    resource.getName(),
                    resource.getType(),
                    resource.getUrl()
                    // resource.getOwner()
                )).collect(Collectors.toList());
    }
}
