package com.example.we_save.domain.user.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.controller.response.UserAuthResponseDto;
import com.example.we_save.domain.user.converter.UserConverter;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.service.NotificationSettingCommandService;
import com.example.we_save.domain.user.service.UserAuthCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthCommandService userAuthCommandService;
    private final NotificationSettingCommandService notificationSettingCommandService;

    @PostMapping("/api/auth/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserAuthResponseDto.JoinResultDTO> join(@RequestBody @Valid UserAuthRequestDto.JoinDto request){
        NotificationSetting notificationSetting = notificationSettingCommandService.createNotificationSetting();
        User user= userAuthCommandService.joinUser(request,notificationSetting);
        return ApiResponse.onPostSuccess(UserConverter.toJoinResultDTO(user));
    }
}
