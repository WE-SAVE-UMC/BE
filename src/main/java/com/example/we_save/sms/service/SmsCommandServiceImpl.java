package com.example.we_save.sms.service;

import com.example.we_save.sms.SmsUtil;
import org.springframework.stereotype.Service;

@Service
public class SmsCommandServiceImpl implements SmsCommandService{
    private final SmsUtil smsUtil;

    public SmsCommandServiceImpl(SmsUtil smsUtil) {
        this.smsUtil = smsUtil;
    }

    @Override
    public void sendSms(String phoneNumber,String code) {
        this.smsUtil.sendOne(phoneNumber, code);
    }
}
