package com.example.we_save.domain.user.controller.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

public class UserAuthRequestDto {
    @Getter
    public static class JoinDto{
        @Size(max= 11)
        String phoneNum;

        @Size(max=16)
        String nickname;

        @Size(max=20)
        String password;
    }

    @Getter
    public static class loginDto{
        @Size(max= 11)
        String phoneNum;

        @Size(max=20)
        String password;
    }
    @Getter
    public static class findUserDto{
        long userId;
    }

    @Getter
    public static class updateUserDto{
        @Size(max=16)
        String nickname;

        String imageUrl;
    }
}
