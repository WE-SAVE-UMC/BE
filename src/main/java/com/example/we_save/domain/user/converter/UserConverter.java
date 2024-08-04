package com.example.we_save.domain.user.converter;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.controller.response.UserAuthResponseDto;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.entity.UserRole;
import com.example.we_save.domain.user.entity.UserStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class UserConverter {

    public static UserAuthResponseDto.JoinResultDTO toJoinResultDto(User user) {
        return UserAuthResponseDto.JoinResultDTO.builder()
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

    public static User toUser(UserAuthRequestDto.JoinDto request, NotificationSetting notificationSetting, BCryptPasswordEncoder bCryptPasswordEncoder) {

        return User.builder()
                .phoneNum(request.getPhoneNum())
                .nickname(request.getNickname())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(UserRole.USER) //default는 USER
                .status(UserStatus.ACTIVE) //default는 ACTIVE
                .imageUrl("http://localhost:8080/profile.jpg")
                .notificationSetting(notificationSetting)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
