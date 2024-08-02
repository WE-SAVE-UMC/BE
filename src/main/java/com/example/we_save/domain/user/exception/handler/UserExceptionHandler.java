package com.example.we_save.domain.user.exception.handler;

import com.example.we_save.global.common.ResponseCode;
import com.example.we_save.global.error.GeneralException;

public class UserExceptionHandler extends GeneralException {

    public UserExceptionHandler(ResponseCode errorCode) { super(errorCode);}
}
