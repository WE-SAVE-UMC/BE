package com.example.we_save.domain.user.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BlockResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlockResultDto {
        Long BlockUserId;
        Long userId;
        Long targetId;
    }
}
