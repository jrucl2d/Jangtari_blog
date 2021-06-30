package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;

public class InvalidAccessTokenException extends BusinessException {
    public InvalidAccessTokenException() {
        this.errorCode = ErrorCode.INVALID_ACCESS_TOKEN;
    }
}
