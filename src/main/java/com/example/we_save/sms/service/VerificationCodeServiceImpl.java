package com.example.we_save.sms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final Map<String, String> codeStore = new HashMap<>(); // 임시 저장소, 실제로는 데이터베이스 사용하도록 리팩토링

    public String generateCode(String phoneNumber) {
        // 인증코드 생성
        Random random = new Random();
        String code = Integer.toString(random.nextInt(888888)+111111);
        codeStore.put(phoneNumber, code);
        return code;
    }

    public boolean validateCode(String phoneNumber, String code) {
        String storedCode = codeStore.get(phoneNumber);
        return code.equals(storedCode);
    }
}
