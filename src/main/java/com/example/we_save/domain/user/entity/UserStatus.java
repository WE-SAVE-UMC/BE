package com.example.we_save.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserStatus {

    ACTIVE("활성"), INACTIVE("비활성"), PENDING("대기"), SUSPENDED("정지"), BANNED("차단"),
    DELETION_REQUESTED("삭제요청"), DELETED("삭제");

    public final String value;

    public static UserStatus of(String value) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.value.equals(value)) {
                return userStatus;
            }
        }
        throw new IllegalArgumentException();
    }
}
