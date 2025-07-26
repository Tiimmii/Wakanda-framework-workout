package com.iam.iam_app.bridge;

import java.util.List;

import org.wakanda.framework.model.User;

public class WakandaUserAdapter extends User {
    public WakandaUserAdapter(com.iam.iam_app.entity.User user) {
        this.setUserId(user.getId().toString());
        this.setName(user.getUsername().toString());
        this.setEmail(user.getEmail().toString());
        this.setRoles(List.of(user.getUserRole().getRole().name()));
    }
}
