package com.example.we_save.apiPayload.code.status;

import com.example.we_save.apiPayload.code.BaseCode;
import com.example.we_save.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    // 일반적인 응답
    _GET_OK(HttpStatus.OK, "COMMON200", "200 OK"),
    _POST_OK(HttpStatus.CREATED,"COMMON201","201 Created."),
    _DELETE_OK(HttpStatus.ACCEPTED,"COMMON202","삭제되었습니다."),
    CANCEL_SUCCESS(HttpStatus.OK, "COMMON204", "취소되었습니다."),
    // 멤버 관련 응답

    // 신고 관련 응답
    REPORT_SUCCESS(HttpStatus.OK, "REPORT200", "신고되었습니다."),
    _PUT_OK(HttpStatus.OK, "COMMON200", "수정되었습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}