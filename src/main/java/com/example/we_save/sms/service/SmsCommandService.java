package com.example.we_save.sms.service;

public interface SmsCommandService {
    public void sendSms(String phoneNumber, String code);
}
