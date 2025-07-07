package com.iam.iam_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wakanda.framework.model.UserPrincipal;

import com.iam.iam_app.entity.User;
import com.iam.iam_app.repositories.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserPrincipal(user);
    }
}
