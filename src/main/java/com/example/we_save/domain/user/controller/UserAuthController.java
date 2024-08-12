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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthCommandService userAuthCommandService;
    private final NotificationSettingCommandService notificationSettingCommandService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "회원가입")
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserAuthResponseDto.JoinResultDto>> join(@RequestBody @Valid UserAuthRequestDto.JoinDto request){
        NotificationSetting notificationSetting = notificationSettingCommandService.createNotificationSetting();
        User user= userAuthCommandService.joinUser(request,notificationSetting);

        if (user == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.onFailure("COMMON409","409 Confilt, 이미 회원가입이 된 회원입니다.",null));
        }
        else{
            ApiResponse<UserAuthResponseDto.JoinResultDto> response = ApiResponse.onPostSuccess(UserConverter.toJoinResultDto(user), SuccessStatus._POST_OK);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid UserAuthRequestDto.loginDto request){
        User user = userAuthCommandService.loginUser(request);

        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.onFailure("COMMON401","401 Unauthorized, 인증 정보가 잘못되었습니다.",user));
        }
        else{
            String token = jwtUtil.createJwt(user.getId().toString(),"USER",24 * 60 * 60 * 1000L);
            ApiResponse<UserAuthResponseDto.loginResultDto> response = ApiResponse.onPostSuccess(UserConverter.toLoginResultDto(user,token),SuccessStatus._POST_OK);
            return ResponseEntity.ok(response);
        }
    }


    @Operation(summary = "로그인 된 회원 정보(마이페이지-프로필 조회)", security = @SecurityRequirement(name="Authorization"))
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getUserInfo(){
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        ApiResponse<UserAuthResponseDto.findUserResultDto> response = ApiResponse.onGetSuccess(UserConverter.toUserResultDto(user));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인 된 회원 정보 수정(마이페이지-프로필 수정)", security = @SecurityRequirement(name="Authorization"))
    @PutMapping("users")
    public ResponseEntity<ApiResponse> updateUserInfo(@RequestBody @Valid UserAuthRequestDto.updateUserDto request){
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        User updateUser = userAuthCommandService.updateUser(user,request.getNickname(),request.getImageUrl());
        ApiResponse<UserAuthResponseDto.findUserResultDto> response = ApiResponse.onGetSuccess(UserConverter.toUserResultDto(updateUser));
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "유효한 전화번호 확인")
    @GetMapping("/check-phone/{number}")
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

    @Operation(summary = "유효한 닉네임 확인")
    @GetMapping("/check-nickname/{nickname}")
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
