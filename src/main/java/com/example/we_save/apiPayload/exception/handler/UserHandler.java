package com.example.we_save.apiPayload.exception.handler;


import com.example.we_save.apiPayload.code.BaseErrorCode;
import com.example.we_save.apiPayload.exception.GeneralException;

public class UserHandler  extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
