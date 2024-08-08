package com.example.we_save.domain.user.service;


import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.converter.UserConverter;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.NotificationSettingRepository;
import com.example.we_save.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        List<User> users  = userRepository.findByPhoneNum(request.getPhoneNum());

        // 똑같은 전화번호를 가진 유저가 없다면 회원가입 성공
        if (users.isEmpty()){
            User newUser = UserConverter.makeUser(request, notificationSetting, bCryptPasswordEncoder);
            return userRepository.save(newUser);
        }
        return null;
    }

    @Override
    public User loginUser(UserAuthRequestDto.loginDto request) {
        List<User> users  = userRepository.findByPhoneNum(request.getPhoneNum());

        // 사용자 존재 여부 및 비밀번호 검증
        if (users.isEmpty() || !bCryptPasswordEncoder.matches(request.getPassword(), users.get(0).getPassword())) {
            return null;
        }
        else{
            //로그인 성공
            return users.get(0);
        }
    }

    @Override
    public User updateUser(User user, String newNickname, String newImageUrl ) {
        user.setImageUrl(newNickname);
        user.setNickname(newImageUrl);
        return userRepository.save(user);
    }

    @Override
    public Boolean isValidPhoneNumber(String phoneNumber) {
        return !userRepository.existsByPhoneNum(phoneNumber);
    }

    @Override
    public Boolean isValidNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Override
    public User findByUserId(long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
