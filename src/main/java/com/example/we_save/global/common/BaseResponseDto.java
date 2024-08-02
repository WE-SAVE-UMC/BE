package com.example.we_save.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponseDto<T> {

    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> BaseResponseDto<T> onSuccess(T data, ResponseCode code) {
        return new BaseResponseDto<>(true, code.getCode(), code.getMessage(), data);
    }

    public static <T> BaseResponseDto<T> onFailure(T errors, ResponseCode code) {
        return new BaseResponseDto<>(false, code.getCode(), code.getMessage(), errors);
    }
}
