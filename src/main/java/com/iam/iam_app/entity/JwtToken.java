package com.iam.iam_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "jwt_tokens")
@Getter
@Setter
@SuperBuilder
public class JwtToken extends BaseEntity {

    @Column(name = "access_token", nullable = false, length = 1000)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, length = 1000)
    private String refreshToken;

    @OneToOne(mappedBy = "jwtToken")
    private User user;

    public JwtToken() {
        super();
    }
}
