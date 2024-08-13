package com.example.we_save.domain.user.service;


import com.example.we_save.apiPayload.util.PasswordUtil;
import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.converter.UserConverter;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.NotificationSettingRepository;
import com.example.we_save.domain.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if(PasswordUtil.isValidPassword(request.getPassword())){
            List<User> users  = userRepository.findByPhoneNum(request.getPhoneNum());

            // 똑같은 전화번호를 가진 유저가 없다면 회원가입 성공
            if (users.isEmpty()){
                User newUser = UserConverter.makeUser(request, notificationSetting, bCryptPasswordEncoder);
                return userRepository.save(newUser);
            }
        }else{
            throw new IllegalArgumentException("유효하지 않은 비밀번호입니다. 비밀번호는 8~16자 길이여야 하며, 숫자, 영문 대소문자, 그리고 특수문자가 포함되어야 합니다.");
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
        return userRepository.findById(userId);
    }
    @Override
    public User getAuthenticatedUserInfo() {
        long userId;
        try {
            String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
            userId = Long.parseLong(userIdStr);
        } catch (Exception e) {
            throw new JwtException("토큰 정보에 해당하는 유저가 없음");
        }

        User user = this.findByUserId(userId);

        if (user == null) {
            throw new JwtException("토큰 정보에 해당하는 유저가 없음");
        }
        return user;
    }

    @Override
    public User findByUserPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNum(phoneNumber).isEmpty() ? null : userRepository.findByPhoneNum(phoneNumber).get(0);
    }

    @Override
    public User updateUserPassword(User user, String newPassword) {
        if(PasswordUtil.isValidPassword(newPassword)){
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            return userRepository.save(user);
        }else{
            throw new IllegalArgumentException("유효하지 않은 비밀번호입니다. 비밀번호는 8~16자 길이여야 하며, 숫자, 영문 대소문자, 그리고 특수문자가 포함되어야 합니다.");
        }
    }



}
