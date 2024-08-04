package com.example.we_save.domain.user.service;

import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;

public interface UserAuthCommandService {
    public User joinUser(UserAuthRequestDto.JoinDto request, NotificationSetting notificationSetting);
    public Boolean isValidPhoneNumber(String phoneNumber);
    public Boolean isValidNickname(String nickname);

}
