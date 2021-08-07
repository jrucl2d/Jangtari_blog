package com.yu.jangtari.api.member.domain;

public enum RoleType {
    USER, ADMIN;
    public static RoleType of(String name) {
        switch (name) {
            case "USER":
                return USER;
            case "ADMIN":
                return ADMIN;
            default:
                throw new IllegalArgumentException("잘못된 role 입니다.");
        }
    }
}
