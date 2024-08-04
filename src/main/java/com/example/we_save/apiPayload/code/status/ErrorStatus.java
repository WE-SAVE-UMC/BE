package com.example.we_save.apiPayload.code.status;

import com.example.we_save.apiPayload.code.BaseErrorCode;
import com.example.we_save.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _CONFLICT(HttpStatus.CONFLICT,"COMMON409","잘못된 입력입니다."),
    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    _NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),

    // Article Error
    _ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    //Food Error
    FOOD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND,"FOOD4001", " food카테고리가 존재하지 않습니다."),

    //Store Error
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND,"STORE4001","store가 존재하지 않습니다."),

    //Mission Error
    MISSION_NOT_FOUND(HttpStatus.BAD_REQUEST,"MISSION4001","mission이 존재하지 않습니다."),

    //MemberMission Error
    MEMBER_MISSION_ALREADY_IN_PROGRESS(HttpStatus.CONFLICT,"MEMBER_MISSION4001","이미 진행중인 미션입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}