package com.example.we_save.sms.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.sms.controller.request.SmsRequestDto;
import com.example.we_save.sms.controller.response.SmsResponseDto;
import com.example.we_save.sms.service.SmsCommandService;
import com.example.we_save.sms.service.VerificationCodeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SmsController {
    private final SmsCommandService smsCommandService;
    private final VerificationCodeServiceImpl verificationCodeService;

    @PostMapping("/api/sms/requests")
    public ApiResponse<SmsResponseDto.SmsRequestResultDto> sendSms(@RequestBody @Valid SmsRequestDto.sendSmsDto request) {
        // 생성된 인증코드를 SMS로 발송
        String verificationCode = verificationCodeService.generateCode(request.getPhoneNum());
        smsCommandService.sendSms(request.getPhoneNum(), verificationCode);

        return ApiResponse.onGetSuccess(SmsResponseDto.SmsRequestResultDto.builder().verificationCode(verificationCode).build());
    }
}
