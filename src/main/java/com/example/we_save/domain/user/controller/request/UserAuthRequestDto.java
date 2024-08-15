package com.example.we_save.domain.user.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
        @NotNull
        String nickname;
    }

    @Getter
    public static class changePasswordDto{
        @Size(max= 11)
        String phoneNum;
        @Size(max=20)
        String password;
    }
}
