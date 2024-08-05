package com.example.we_save.domain.user.controller.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class UserAuthResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResultDto{
        Long userId;
        LocalDateTime createAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidResultDto{
        Boolean isValid;
        String message;
    }

}
