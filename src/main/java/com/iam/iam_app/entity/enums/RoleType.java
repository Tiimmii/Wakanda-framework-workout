package com.iam.iam_app.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleType {
    ADMIN,
    AGENT,
    CUSTOMER;

    @JsonCreator
    public static RoleType from(String value) {
        return RoleType.valueOf(value.toUpperCase());
    }
}