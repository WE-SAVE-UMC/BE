package com.example.we_save.domain.user.controller;

import com.example.we_save.domain.user.application.UserService;
import com.example.we_save.domain.user.controller.response.UserResponseDto;
import com.example.we_save.global.common.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // BaseResponseDto 사용 예시 - 유저 생성
    // TODO: 나중에 삭제해야 됨
    @PostMapping
    public BaseResponseDto<UserResponseDto> registerUser() {

        /** userService의 메서드인 save도 같은 타입(BaseResponseDto<UserResponseDto>)으로 반환 해야됨 */
        // return userService.save

        return null;
    }


}
