package com.example.we_save.domain.post.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;

@RequiredArgsConstructor
public enum PostStatus {

    PROCESSING("처리중"), COMPLETED("완료중");

    public final String value;

    public static PostStatus of(String value) {
        for (PostStatus postStatus : PostStatus.values()) {
            if (postStatus.value.equals(value)) {
                return postStatus;
            }
        }
        throw new IllegalArgumentException();
    }
}
