package com.example.we_save.domain.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PostStatus {

    PROCESSING("처리중"), COMPLETED("완료중");

    private final String value;

    public static PostStatus of(String value) {
        for (PostStatus postStatus : PostStatus.values()) {
            if (postStatus.value.equals(value)) {
                return postStatus;
            }
        }
        throw new IllegalArgumentException();
    }
}