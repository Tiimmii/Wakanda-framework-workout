package com.iam.iam_app.bridge;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wakanda.framework.exception.BaseException;
import org.wakanda.framework.model.UserPrincipal;

public class AuthHandler {
    public UserPrincipal getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new BaseException(401, "User not authenticated");
        }
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return principal;
    }
}
