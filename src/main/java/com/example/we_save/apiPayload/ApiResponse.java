package com.example.we_save.apiPayload;

import com.example.we_save.apiPayload.code.BaseCode;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    // 성공한 경우 응답 생성

    public static <T> ApiResponse<T> onGetSuccess(T result){
        return new ApiResponse<>(true, SuccessStatus._GET_OK.getCode() , SuccessStatus._GET_OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> onPostSuccess(T result, SuccessStatus PostOk){
        return new ApiResponse<>(true, SuccessStatus._POST_OK.getCode() , SuccessStatus._POST_OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> of(BaseCode code, T result){
            return new ApiResponse<>(true, code.getReasonHttpStatus().getCode() , code.getReasonHttpStatus().getMessage(), result);
    }

    public static <T> ApiResponse<T> onDeleteSuccess(T result){
        return new ApiResponse<>(true, SuccessStatus._DELETE_OK.getCode() , SuccessStatus._DELETE_OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> onReportSuccess(T result){
        return new ApiResponse<>(true, SuccessStatus.REPORT_SUCCESS.getCode(), SuccessStatus.REPORT_SUCCESS.getMessage(), result);
    }

    // 실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure(String code, String message, T data){
        return new ApiResponse<>(false, code, message, data);
    }
}