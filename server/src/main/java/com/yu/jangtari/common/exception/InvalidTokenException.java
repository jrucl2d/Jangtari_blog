package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;

public class InvalidTokenException extends BusinessException
{
    public InvalidTokenException() {
        this.errorCode = ErrorCode.INVALID_TOKEN_ERROR;
    }
}
