package com.example.we_save.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {

    ADMIN("관리자"), USER("회원");

    private final String value;

    public static UserRole of(String value) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.value.equals(value)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException();
    }
}
