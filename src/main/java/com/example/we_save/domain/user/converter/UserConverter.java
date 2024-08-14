package com.example.we_save.domain.user.converter;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.controller.response.UserAuthResponseDto;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.entity.UserRole;
import com.example.we_save.domain.user.entity.UserStatus;
import com.example.we_save.image.entity.Image;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class UserConverter {

    public static UserAuthResponseDto.JoinResultDto toJoinResultDto(User user) {
        return UserAuthResponseDto.JoinResultDto.builder()
                .userId(user.getId())
                .createAt(user.getCreatedAt())
                .build();
    }
    public static UserAuthResponseDto.ValidResultDto toValidResultDto(Boolean isValid,String message){
        return UserAuthResponseDto.ValidResultDto.builder()
                .isValid(isValid)
                .message(message)
                .build();
    }

    public static UserAuthResponseDto.loginResultDto toLoginResultDto(User user, String token) {
        return UserAuthResponseDto.loginResultDto.builder()
                .token("Bearer " + token)
                .userId(user.getId())
                .build();
    }

    public static UserAuthResponseDto.findUserResultDto toUserResultDto(User user) {
        return UserAuthResponseDto.findUserResultDto.builder()
                .userId(user.getId())
                .status(user.getStatus())
                .profileImage(user.getProfileImage())
                .nickname(user.getNickname())
                .phoneNum(user.getPhoneNum())
                .build();
    }

    public static UserAuthResponseDto.findUserIdResultDto toUserIdResultDto(User user) {
        return UserAuthResponseDto.findUserIdResultDto.builder()
                .userId(user.getId())
                .build();
    }




    public static User makeUser(UserAuthRequestDto.JoinDto request, NotificationSetting notificationSetting, BCryptPasswordEncoder bCryptPasswordEncoder) {
        Image image = new Image();
        image.setName("default_profile.jpg");
        image.setFilePath("/files/user/default_profile.jpg");

        return User.builder()
                .phoneNum(request.getPhoneNum())
                .nickname(request.getNickname())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role("USER") //default는 USER
                .status(UserStatus.ACTIVE) //default는 ACTIVE
                .profileImage(image)
                .notificationSetting(notificationSetting)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
