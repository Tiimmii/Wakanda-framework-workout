package com.iam.iam_app.security;

import com.iam.iam_app.entity.User;
import com.iam.iam_app.repositories.UserRepository;
import com.iam.iam_app.service.JwtService;
import com.iam.iam_app.bridge.WakandaUserAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.wakanda.framework.model.UserPrincipal;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = jwtService.extractUserId(token);
        Optional<User> userOpt = userRepository.findById(Integer.parseInt(userId));
        if (!userOpt.isPresent()) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = userOpt.get();
        UserPrincipal principal = new UserPrincipal(new WakandaUserAdapter(user));
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
