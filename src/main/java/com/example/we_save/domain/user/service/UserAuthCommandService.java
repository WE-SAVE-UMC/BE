package com.example.we_save.domain.user.service;

import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;

import java.util.Optional;

public interface UserAuthCommandService {
    public User joinUser(UserAuthRequestDto.JoinDto request, NotificationSetting notificationSetting);
    public User loginUser(UserAuthRequestDto.loginDto request);
    public Boolean isValidPhoneNumber(String phoneNumber);
    public Boolean isValidNickname(String nickname);
    public User findByUserId(long userId);
    public User updateUser(User user,String newNickname,String imageUrl);
    public User getAuthenticatedUserInfo();
    public User findByUserPhoneNumber(String phoneNumber);
    public User updateUserPassword(User user, String newPassword);
}
