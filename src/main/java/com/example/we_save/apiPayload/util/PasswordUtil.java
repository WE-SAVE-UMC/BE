package com.example.we_save.apiPayload.util;

import java.util.regex.Pattern;

public class PasswordUtil {
    // 허용된 특수문자
    private static final String SPECIAL_CHARACTERS = ":!\"#$%&'()*+,-:;?@[\\]^_₩{|}";

    // 비밀번호 유효성 검사 정규식
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[" + Pattern.quote(SPECIAL_CHARACTERS) + "])" +
                    "(?=\\S+$).{8,16}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValidPassword(String password) {
        return pattern.matcher(password).matches();
    }
}
