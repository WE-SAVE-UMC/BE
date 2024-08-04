package com.example.we_save.domain.user.service;


import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.converter.UserConverter;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.NotificationSettingRepository;
import com.example.we_save.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthCommandServiceImpl implements UserAuthCommandService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserAuthCommandServiceImpl(UserRepository userRepository,NotificationSettingRepository notificationSettingRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User joinUser(UserAuthRequestDto.JoinDto request,NotificationSetting notificationSetting) {
        User newUser = UserConverter.toUser(request, notificationSetting, bCryptPasswordEncoder);
        return userRepository.save(newUser);
    }

}
