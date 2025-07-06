package com.iam.iam_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (enable in production)
            // .authorizeHttpRequests(auth -> auth
            //     .requestMatchers("/auth/login", "/auth/register", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // allow login, register, swagger
            //     .anyRequest().authenticated()
            // )
            .formLogin(form -> form.disable()) // ❌ Disable Spring Boot's default login form
            .httpBasic(basic -> basic.disable()); // Optional: disable basic auth if not needed

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
