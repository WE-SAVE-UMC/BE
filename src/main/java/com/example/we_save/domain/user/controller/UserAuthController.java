package com.example.we_save.domain.user.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.controller.response.UserAuthResponseDto;
import com.example.we_save.domain.user.converter.UserConverter;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.service.NotificationSettingCommandService;
import com.example.we_save.domain.user.service.UserAuthCommandService;
import com.example.we_save.jwt.JWTUtil;
import com.example.we_save.jwt.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthCommandService userAuthCommandService;
    private final NotificationSettingCommandService notificationSettingCommandService;
    private final JWTUtil jwtUtil;

    @PostMapping("/api/auth/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserAuthResponseDto.JoinResultDto> join(@RequestBody @Valid UserAuthRequestDto.JoinDto request){
        NotificationSetting notificationSetting = notificationSettingCommandService.createNotificationSetting();
        User user= userAuthCommandService.joinUser(request,notificationSetting);
        return ApiResponse.onPostSuccess(UserConverter.toJoinResultDto(user), SuccessStatus._POST_OK);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid UserAuthRequestDto.loginDto request){
        User user = userAuthCommandService.loginUser(request);

        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.onFailure("COMMON401","401 Unauthorized, 인증 정보가 잘못되었습니다.",user));
        }
        else{
            String token = jwtUtil.createJwt(request.getPhoneNum(),"USER",24 * 60 * 60 * 1000L);
            ApiResponse<UserAuthResponseDto.loginResultDto> response = ApiResponse.onPostSuccess(UserConverter.toLoginResultDto(user,token),SuccessStatus._POST_OK);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/api/auth/check-phone/{number}")
    public ResponseEntity<ApiResponse<UserAuthResponseDto.ValidResultDto>>isValidPhoneNum(@PathVariable String number){
        Boolean isValid = userAuthCommandService.isValidPhoneNumber(number);

        if (isValid) {
            ApiResponse<UserAuthResponseDto.ValidResultDto> response = ApiResponse.onGetSuccess(
                    UserConverter.toValidResultDto(isValid, "유효한 전화번호 입니다."));
            return ResponseEntity.ok(response);
        }
        else{
            ApiResponse<UserAuthResponseDto.ValidResultDto> response = ApiResponse.onFailure(
                    "COMMON409", "409 Conflict", UserConverter.toValidResultDto(isValid, "중복된 전화번호 입니다."));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    @GetMapping("/api/auth/check-nickname/{nickname}")
    public ResponseEntity<ApiResponse<UserAuthResponseDto.ValidResultDto>>isValidNickame(@PathVariable String nickname){
        Boolean isValid = userAuthCommandService.isValidNickname(nickname);

        if (isValid) {
            ApiResponse<UserAuthResponseDto.ValidResultDto> response = ApiResponse.onGetSuccess(
                    UserConverter.toValidResultDto(isValid, "유효한 닉네임 입니다."));
            return ResponseEntity.ok(response);
        }
        else{
            ApiResponse<UserAuthResponseDto.ValidResultDto> response = ApiResponse.onFailure(
                    "COMMON409", "409 Conflict", UserConverter.toValidResultDto(isValid, "중복된 닉네임 입니다."));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

}
