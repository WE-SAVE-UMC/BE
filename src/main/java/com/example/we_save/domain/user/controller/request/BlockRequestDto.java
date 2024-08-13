package com.example.we_save.domain.user.controller.request;

import lombok.Getter;

public class BlockRequestDto {
    @Getter
    public static class BlockDto{
        long targetId;
    }
}
