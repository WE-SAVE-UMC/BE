package com.example.we_save.sms.service;

public interface VerificationCodeService {
    public String generateCode(String phoneNumber);
    public boolean validateCode(String phoneNumber, String code);
}
