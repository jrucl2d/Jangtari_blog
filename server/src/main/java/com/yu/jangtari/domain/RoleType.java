package com.yu.jangtari.domain;

import com.yu.jangtari.common.exception.InvalidAccessTokenException;

public enum RoleType {
    USER, ADMIN;
    public static RoleType of(String name) {
        switch (name) {
            case "USER":
                return USER;
            case "ADMIN":
                return ADMIN;
            default:
                throw new InvalidAccessTokenException();
        }
    }
}
