package com.iam.iam_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import com.iam.iam_app.service.CustomUserDetailService;

@Configuration
public class SecurityConfig {
    // private CustomUserDetailService customUserDetailService;

    // @Autowired
    // public SecurityConfig(CustomUserDetailService customUserDetailService) {
    //     this.customUserDetailService = customUserDetailService;
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests(auth -> auth
                        .antMatchers(
                                "/api/admin/create-user",
                                "/api/admin/agents",
                                "/api/admin/customers",
                                "/api/admin/users",
                                "/api/admin/users",
                                "/api/customer/resource",
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/admin/users/update-permissions",
                                "/api/agent/create-resource",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // or "http://localhost:3000" for React frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    // @Bean
    // DaoAuthenticationProvider authenticationProvider(){
    // DaoAuthenticationProvider daoAuthenticationProvider = new
    // DaoAuthenticationProvider();
    // daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    // daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
    // return daoAuthenticationProvider;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
