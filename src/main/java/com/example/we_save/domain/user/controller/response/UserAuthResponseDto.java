package com.example.we_save.domain.user.controller.response;

import com.example.we_save.domain.user.entity.UserStatus;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class loginResultDto{
        Long userId;
        String token;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class findUserResultDto{
        Long userId;
        String phone_num;
        String nickname;
        String imageUrl;
        UserStatus status;
    }

}
