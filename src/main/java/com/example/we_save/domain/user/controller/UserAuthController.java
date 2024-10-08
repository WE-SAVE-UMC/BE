package com.example.we_save.domain.user.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.user.controller.request.UserAuthRequestDto;
import com.example.we_save.domain.user.controller.response.UserAuthResponseDto;
import com.example.we_save.domain.user.converter.UserConverter;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.entity.UserStatus;
import com.example.we_save.domain.user.service.NotificationSettingCommandService;
import com.example.we_save.domain.user.service.UserAuthCommandService;
import com.example.we_save.image.entity.Image;
import com.example.we_save.image.service.ImageService;
import com.example.we_save.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthCommandService userAuthCommandService;
    private final ImageService imageService;
    private final NotificationSettingCommandService notificationSettingCommandService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "회원가입")
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserAuthResponseDto.JoinResultDto>> join(@RequestBody @Valid UserAuthRequestDto.JoinDto request) {
        NotificationSetting notificationSetting = notificationSettingCommandService.createNotificationSetting();
        try {
            Image newProfileImage = imageService.saveDefaultProfileImage(); //파일서버에 프로필 이미지 등록
            User user = userAuthCommandService.joinUser(request, notificationSetting, newProfileImage);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.onFailure("COMMON409", "이미 회원가입이 된 회원입니다.", null));
            } else {

                ApiResponse<UserAuthResponseDto.JoinResultDto> response = ApiResponse.onPostSuccess(UserConverter.toJoinResultDto(user), SuccessStatus._POST_OK);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400", e.getMessage(), null));
        }
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid UserAuthRequestDto.loginDto request) {
        User user = userAuthCommandService.loginUser(request);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.onFailure("COMMON401", "인증 정보가 잘못되었습니다.", null));
        } else {
            if (user.getStatus().equals(UserStatus.SUSPENDED)) { //유저의 상태가 정지일 경우
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.onFailure("COMMON403", "정지된 회원입니다.", UserConverter.toUserStatusResultDto(user)));
            }
            if (user.getStatus().equals(UserStatus.INACTIVE)) { //유저의 상태가 비활성화일 경우
                userAuthCommandService.activateUser(user); //비활성화 해제 -> 로그인 가능
            }

            String token = jwtUtil.createJwt(user.getId().toString(), "USER", 24 * 60 * 60 * 1000L);
            ApiResponse<UserAuthResponseDto.loginResultDto> response = ApiResponse.onPostSuccess(UserConverter.toLoginResultDto(user, token), SuccessStatus._POST_OK);
            return ResponseEntity.ok(response);

        }
    }


    @Operation(summary = "로그인 된 회원 정보(마이페이지-프로필 조회)", security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getUserInfo() {
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        ApiResponse<UserAuthResponseDto.findUserResultDto> response = ApiResponse.onGetSuccess(UserConverter.toUserResultDto(user));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인 된 회원 정보 수정(마이페이지-프로필 수정)", security = @SecurityRequirement(name = "Authorization"))
    @PutMapping(value = "/users")
    public ResponseEntity<ApiResponse> updateUserInfo(@RequestPart("nickname") String nickname,
                                                      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        try {

            imageService.deleteProfileImage(user.getProfileImage().getId(), user); //파일서버에서 기존 프로필 이미지 삭제
            Image newProfileImage;

            if (profileImage == null || profileImage.isEmpty()) {
                newProfileImage = imageService.saveDefaultProfileImage(); //이미지 등록 안할시 디폴트 이미지 등록
            } else {
                newProfileImage = imageService.saveProfileImage(profileImage, user); //파일서버에 프로필 이미지 등록
            }
            User updateUser = userAuthCommandService.updateUser(user, nickname, newProfileImage); //유저 정보 업데이트
            ApiResponse<UserAuthResponseDto.findUserResultDto> response = ApiResponse.onGetSuccess(UserConverter.toUserResultDto(updateUser));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Image newProfileImage = imageService.saveDefaultProfileImage();
            User updateUser = userAuthCommandService.updateUser(user, nickname, newProfileImage); //유저 정보 업데이트
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400", "파일 업로드 오류", UserConverter.toUserResultDto(updateUser)));
        }
    }

    @Operation(summary = "회원 탈퇴", security = @SecurityRequirement(name = "Authorization"))
    @DeleteMapping(value = "/users")
    public ResponseEntity<ApiResponse> deleteUserInfo() {
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        if (user.getStatus().equals(UserStatus.ACTIVE)) {
            userAuthCommandService.inactiveUser(user);
            ApiResponse<UserAuthResponseDto.findUserStatusResultDto> response = ApiResponse.onGetSuccess(UserConverter.toUserStatusResultDto(user));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.onFailure("COMMON403", "탈퇴 권한이 없는 회원입니다.", UserConverter.toUserStatusResultDto(user)));
        }

    }

    @Operation(summary = "유효한 전화번호 확인")
    @GetMapping("/check-phone/{number}")
    public ResponseEntity<ApiResponse<UserAuthResponseDto.ValidResultDto>> isValidPhoneNum(@PathVariable String number) {
        Boolean isValid = userAuthCommandService.isValidPhoneNumber(number);

        if (isValid) {
            ApiResponse<UserAuthResponseDto.ValidResultDto> response = ApiResponse.onGetSuccess(
                    UserConverter.toValidResultDto(isValid, "유효한 전화번호 입니다."));
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserAuthResponseDto.ValidResultDto> response = ApiResponse.onFailure(
                    "COMMON409", "409 Conflict", UserConverter.toValidResultDto(isValid, "중복된 전화번호 입니다."));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }


    @Operation(summary = "유효한 닉네임 확인")
    @GetMapping("/check-nickname/{nickname}")
    public ResponseEntity<ApiResponse<UserAuthResponseDto.ValidResultDto>> isValidNickame(@PathVariable String nickname) {
        Boolean isValid = userAuthCommandService.isValidNickname(nickname);

        if (isValid) {
            ApiResponse<UserAuthResponseDto.ValidResultDto> response = ApiResponse.onGetSuccess(
                    UserConverter.toValidResultDto(isValid, "유효한 닉네임 입니다."));
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserAuthResponseDto.ValidResultDto> response = ApiResponse.onFailure(
                    "COMMON409", "409 Conflict", UserConverter.toValidResultDto(isValid, "중복된 닉네임 입니다."));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @Operation(summary = "비밀번호 재설정")
    @PatchMapping("/users")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody @Valid UserAuthRequestDto.changePasswordDto request) {
        // 사용자 정보 조회
        User user = userAuthCommandService.findByUserPhoneNumber(request.getPhoneNum());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.onFailure("COMMON401", "해당 전화번호의 회원이 존재하지 않습니다. 회원가입 해주세요. ", null));
        }
        try {
            User updateUser = userAuthCommandService.updateUserPassword(user, request.getPassword());
            return ResponseEntity.ok(ApiResponse.onGetSuccess(UserConverter.toUserIdResultDto(updateUser)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400", e.getMessage(), null));
        }
    }
}
