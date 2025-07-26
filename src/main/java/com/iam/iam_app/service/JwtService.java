package com.iam.iam_app.service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.iam.iam_app.entity.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtService {

    private final SecretKey jwtSecret;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("role", user.getUserRole().getRole().toString())
                .setIssuedAt(new java.util.Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(jwtSecret)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new java.util.Date())
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(jwtSecret)
                .compact();
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("JWT malformed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
        }

        return false;
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // user ID is stored in `sub` claim
    }
}
