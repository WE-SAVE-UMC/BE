package com.example.we_save.sms.controller.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

public class SmsRequestDto {
    @Getter
    public static class sendSmsDto{
        @Size(max= 11)
        String phoneNum;
    }

    @Getter
    public static class checkSmsDto{
        @Size(max= 11)
        String phoneNum;
        @Size(max=6)
        String verificationCode;
    }

}
